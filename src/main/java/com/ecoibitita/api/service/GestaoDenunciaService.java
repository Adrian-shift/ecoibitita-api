package com.ecoibitita.api.service;

import com.ecoibitita.api.dto.AlterarStatusRequest;
import com.ecoibitita.api.dto.DenunciaGestaoDetalheResponse;
import com.ecoibitita.api.dto.DenunciaGestaoDetalheResponse.Movimentacao;
import com.ecoibitita.api.dto.DenunciaGestaoResponse;
import com.ecoibitita.api.dto.PaginaResponse;
import com.ecoibitita.api.dto.ResumoDenunciasResponse;
import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.denuncia.HistoricoDenuncia;
import com.ecoibitita.api.model.denuncia.Imagem;
import com.ecoibitita.api.model.denuncia.Localizacao;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.DenunciaRepository;
import com.ecoibitita.api.repository.HistoricoDenunciaRepository;
import com.ecoibitita.api.repository.UsuarioRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class GestaoDenunciaService {

    private static final ZoneId FUSO = ZoneId.of("America/Bahia");
    private static final int MAX_TAMANHO_PAGINA = 50;

    private final DenunciaRepository denunciaRepository;
    private final HistoricoDenunciaRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoria;

    public GestaoDenunciaService(DenunciaRepository denunciaRepository,
                                 HistoricoDenunciaRepository historicoRepository,
                                 UsuarioRepository usuarioRepository,
                                 AuditoriaService auditoria) {
        this.denunciaRepository = denunciaRepository;
        this.historicoRepository = historicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoria = auditoria;
    }

    // ---------- leitura ----------

    @Transactional(readOnly = true)
    public ResumoDenunciasResponse resumo() {
        LocalDate hoje = LocalDate.now(FUSO);
        LocalDate inicio = hoje.minusDays(29); // janela de 30 dias contando hoje
        long ultimos = denunciaRepository.countByDataRegistroGreaterThanEqualAndDataRegistroLessThan(
                inicio.atStartOfDay(FUSO).toOffsetDateTime(),
                hoje.plusDays(1).atStartOfDay(FUSO).toOffsetDateTime());

        return new ResumoDenunciasResponse(
                denunciaRepository.count(),
                ultimos,
                historicoRepository.contarUsuariosEnvolvidos(),
                inicio,
                hoje);
    }

    @Transactional(readOnly = true)
    public PaginaResponse<DenunciaGestaoResponse> listar(String busca, StatusDenuncia status,
                                                         Integer categoriaId, String bairro,
                                                         int pagina, int tamanho) {
        int p = Math.max(pagina, 0);
        int t = Math.min(Math.max(tamanho, 1), MAX_TAMANHO_PAGINA);

        // filtros opcionais: o que vier vazio é ignorado
        Specification<Denuncia> filtros = (root, query, cb) -> {
            List<Predicate> condicoes = new ArrayList<>();
            if (busca != null && !busca.isBlank()) {
                String termo = "%" + busca.trim().toLowerCase() + "%";
                condicoes.add(cb.or(
                        cb.like(cb.lower(root.get("protocolo")), termo),
                        cb.like(cb.lower(root.get("localizacao").get("endereco")), termo)));
            }
            if (status != null) {
                condicoes.add(cb.equal(root.get("status"), status));
            }
            if (categoriaId != null) {
                condicoes.add(cb.equal(root.get("categoria").get("id"), categoriaId));
            }
            if (bairro != null && !bairro.isBlank()) {
                condicoes.add(cb.equal(
                        cb.lower(root.get("localizacao").get("bairro")),
                        bairro.trim().toLowerCase()));
            }
            return cb.and(condicoes.toArray(new Predicate[0]));
        };

        Page<Denuncia> resultado = denunciaRepository.findAll(filtros,
                PageRequest.of(p, t, Sort.by(Sort.Direction.DESC, "dataRegistro", "id")));

        return new PaginaResponse<>(
                resultado.getContent().stream().map(DenunciaGestaoResponse::de).toList(),
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.isLast());
    }

    // @Transactional porque fotos e histórico são carregados sob demanda
    @Transactional(readOnly = true)
    public DenunciaGestaoDetalheResponse detalhe(Integer id) {
        return montarDetalhe(buscar(id));
    }

    // ---------- alteração de status ----------

    @Transactional
    public DenunciaGestaoDetalheResponse alterarStatus(Integer id, AlterarStatusRequest req, Integer idAutor) {
        Usuario autor = usuarioRepository.findById(idAutor)
                .orElseThrow(() -> erro(HttpStatus.UNAUTHORIZED, "Sessão inválida"));
        Denuncia denuncia = buscar(id);

        StatusDenuncia anterior = denuncia.getStatus();
        StatusDenuncia novo = req.status();
        if (!permitidos(anterior).contains(novo)) {
            throw erro(HttpStatus.BAD_REQUEST,
                    "Não é possível mudar de \"" + anterior.getDescricao() + "\" para \"" + novo.getDescricao() + "\"");
        }

        String observacao = req.observacao() == null ? null : req.observacao().trim();
        if (observacao != null && observacao.isEmpty()) observacao = null;
        if (novo == StatusDenuncia.CANCELADA && observacao == null) {
            throw erro(HttpStatus.BAD_REQUEST, "Informe o motivo do cancelamento");
        }

        denuncia.alterarStatus(novo, autor, observacao); // já grava o histórico
        denunciaRepository.saveAndFlush(denuncia);

        auditoria.registrar(autor, "ALTERAR_STATUS", "Denuncia", denuncia.getId(),
                anterior.getDescricao() + " -> " + novo.getDescricao());
        return montarDetalhe(denuncia);
    }

    /** Só para frente (ou cancelar). Resolvida e Cancelada são finais. */
    private List<StatusDenuncia> permitidos(StatusDenuncia atual) {
        if (atual == StatusDenuncia.RESOLVIDA || atual == StatusDenuncia.CANCELADA) {
            return List.of();
        }
        List<StatusDenuncia> lista = new ArrayList<>();
        for (StatusDenuncia s : StatusDenuncia.values()) {
            if (s.ordinal() > atual.ordinal()) lista.add(s);
        }
        return lista;
    }

    // ---------- apoio ----------

    private Denuncia buscar(Integer id) {
        return denunciaRepository.findById(id)
                .orElseThrow(() -> erro(HttpStatus.NOT_FOUND, "Denúncia não encontrada"));
    }

    private DenunciaGestaoDetalheResponse montarDetalhe(Denuncia d) {
        Localizacao l = d.getLocalizacao();
        List<Movimentacao> historico = d.getHistorico().stream()
                .map(this::movimentacao)
                .toList();

        return new DenunciaGestaoDetalheResponse(
                d.getId(),
                d.getProtocolo(),
                d.getStatus().getDescricao(),
                d.getStatus().name(),
                d.getCategoria().getNome(),
                d.getDescricao(),
                l.getEndereco(),
                l.getBairro(),
                l.getPontoReferencia(),
                l.getLatitude(),
                l.getLongitude(),
                d.getImagens().stream().map(Imagem::getUrlImagem).toList(),
                d.getDataRegistro(),
                d.getDataAtualizacao(),
                historico,
                permitidos(d.getStatus()).stream().map(StatusDenuncia::name).toList());
    }

    private Movimentacao movimentacao(HistoricoDenuncia h) {
        return new Movimentacao(
                h.getStatusNovo().getDescricao(),
                h.getStatusNovo().name(),
                h.getUsuario().getNome(),
                h.getObservacao(),
                h.getAlteradoEm());
    }

    private ResponseStatusException erro(HttpStatus status, String mensagem) {
        return new ResponseStatusException(status, mensagem);
    }
}
