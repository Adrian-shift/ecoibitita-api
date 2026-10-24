package com.ecoibitita.api.service;

import com.ecoibitita.api.dto.DenunciaDetalheResponse;
import com.ecoibitita.api.dto.DenunciaDetalheResponse.Etapa;
import com.ecoibitita.api.dto.DenunciaResumoResponse;
import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.denuncia.HistoricoDenuncia;
import com.ecoibitita.api.model.denuncia.Imagem;
import com.ecoibitita.api.model.denuncia.Localizacao;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.repository.DenunciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoibitita.api.dto.DenunciaPublicaResponse;
import com.ecoibitita.api.dto.PaginaResponse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ConsultaDenunciaService {

    private static final int MAX_PROTOCOLOS = 50;
    private static final int MAX_TAMANHO_PAGINA = 50;

    // ordem normal do atendimento (Cancelada é tratada à parte)
    private static final List<StatusDenuncia> FLUXO = List.of(
            StatusDenuncia.RECEBIDA,
            StatusDenuncia.EM_ANALISE,
            StatusDenuncia.EM_ATENDIMENTO,
            StatusDenuncia.RESOLVIDA);

    private final DenunciaRepository denunciaRepository;

    public ConsultaDenunciaService(DenunciaRepository denunciaRepository) {
        this.denunciaRepository = denunciaRepository;
    }

    // @Transactional porque imagens e histórico são carregados sob demanda
    // (open-in-view está desligado) e o DTO é montado aqui dentro.
    @Transactional(readOnly = true)
    public DenunciaDetalheResponse buscarPorProtocolo(String protocolo) {
        Denuncia d = denunciaRepository.findByProtocolo(normalizar(protocolo))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Denúncia não encontrada"));

        Localizacao l = d.getLocalizacao();
        return new DenunciaDetalheResponse(
                d.getProtocolo(),
                d.getStatus().getDescricao(),
                d.getCategoria().getNome(),
                d.getDescricao(),
                l.getEndereco(),
                l.getBairro(),
                d.getImagens().stream().map(Imagem::getUrlImagem).toList(),
                d.getDataRegistro(),
                d.getDataAtualizacao(),
                d.isAnonima(),
                linhaDoTempo(d));
    }

    @Transactional(readOnly = true)
    public List<DenunciaResumoResponse> resumos(List<String> protocolos) {
        if (protocolos.size() > MAX_PROTOCOLOS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe no máximo " + MAX_PROTOCOLOS + " protocolos");
        }
        List<String> normalizados = protocolos.stream().map(this::normalizar).distinct().toList();

        return denunciaRepository.findByProtocoloIn(normalizados).stream()
                .sorted(Comparator.comparing(Denuncia::getDataRegistro).reversed())
                .map(DenunciaResumoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaginaResponse<DenunciaPublicaResponse> listarPublicas(StatusDenuncia status, String bairro,
                                                                  int pagina, int tamanho) {
        int p = Math.max(pagina, 0);
        int t = Math.min(Math.max(tamanho, 1), MAX_TAMANHO_PAGINA);

        // filtros opcionais: o que vier nulo é ignorado
        Specification<Denuncia> filtros = (root, query, cb) -> {
            List<Predicate> condicoes = new ArrayList<>();
            if (status != null) {
                condicoes.add(cb.equal(root.get("status"), status));
            }
            if (bairro != null && !bairro.isBlank()) {
                condicoes.add(cb.like(
                        cb.lower(root.get("localizacao").get("bairro")),
                        "%" + bairro.trim().toLowerCase() + "%"));
            }
            return cb.and(condicoes.toArray(new Predicate[0]));
        };

        Page<Denuncia> resultado = denunciaRepository.findAll(
                filtros, PageRequest.of(p, t, Sort.by(Sort.Direction.DESC, "dataRegistro")));

        return new PaginaResponse<>(
                resultado.getContent().stream().map(DenunciaPublicaResponse::de).toList(),
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.isLast());
    }

    private List<Etapa> linhaDoTempo(Denuncia d) {
        StatusDenuncia atual = d.getStatus();
        List<Etapa> etapas = new ArrayList<>();

        if (atual == StatusDenuncia.CANCELADA) {
            // mostra só o que aconteceu e termina em "Cancelada"
            for (StatusDenuncia s : FLUXO) {
                OffsetDateTime data = dataDoStatus(d, s);
                if (data != null) etapas.add(new Etapa(s.getDescricao(), "CONCLUIDA", data));
            }
            etapas.add(new Etapa(atual.getDescricao(), "ATUAL", dataDoStatus(d, atual)));
            return etapas;
        }

        int posicaoAtual = FLUXO.indexOf(atual);
        for (int i = 0; i < FLUXO.size(); i++) {
            StatusDenuncia s = FLUXO.get(i);
            String situacao;
            if (i < posicaoAtual || (i == posicaoAtual && atual == StatusDenuncia.RESOLVIDA)) {
                situacao = "CONCLUIDA";
            } else if (i == posicaoAtual) {
                situacao = "ATUAL";
            } else {
                situacao = "AGUARDANDO";
            }
            etapas.add(new Etapa(s.getDescricao(), situacao, i <= posicaoAtual ? dataDoStatus(d, s) : null));
        }
        return etapas;
    }

    private OffsetDateTime dataDoStatus(Denuncia d, StatusDenuncia status) {
        if (status == StatusDenuncia.RECEBIDA) return d.getDataRegistro();
        return d.getHistorico().stream()
                .filter(h -> h.getStatusNovo() == status)
                .map(HistoricoDenuncia::getAlteradoEm)
                .reduce((primeiro, ultimo) -> ultimo)
                .orElse(null);
    }

    private String normalizar(String protocolo) {
        return protocolo.trim().toUpperCase();
    }
}
