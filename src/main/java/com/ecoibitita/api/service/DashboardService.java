package com.ecoibitita.api.service;

import com.ecoibitita.api.dto.DashboardResumoResponse;
import com.ecoibitita.api.dto.DenunciaGestaoResponse;
import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.repository.DenunciaRepository;
import com.ecoibitita.api.repository.HistoricoDenunciaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@Service
public class DashboardService {

    private static final ZoneId FUSO = ZoneId.of("America/Bahia");
    private static final int META_HORAS = 24;
    private static final int MAX_TABELA = 50;
    private static final int MAX_MAPA = 500;

    private final DenunciaRepository denunciaRepository;
    private final HistoricoDenunciaRepository historicoRepository;

    public DashboardService(DenunciaRepository denunciaRepository,
                            HistoricoDenunciaRepository historicoRepository) {
        this.denunciaRepository = denunciaRepository;
        this.historicoRepository = historicoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResumoResponse resumo() {
        Map<StatusDenuncia, Long> porStatus = new EnumMap<>(StatusDenuncia.class);
        for (Object[] linha : denunciaRepository.contarPorStatus()) {
            porStatus.put((StatusDenuncia) linha[0], (Long) linha[1]);
        }
        long total = porStatus.values().stream().mapToLong(Long::longValue).sum();
        long resolvidas = porStatus.getOrDefault(StatusDenuncia.RESOLVIDA, 0L);
        int taxa = total == 0 ? 0 : (int) Math.round(resolvidas * 100.0 / total);

        // mês atual x mês anterior
        LocalDate primeiroDoMes = LocalDate.now(FUSO).withDayOfMonth(1);
        OffsetDateTime inicioMes = primeiroDoMes.atStartOfDay(FUSO).toOffsetDateTime();
        OffsetDateTime inicioProximo = primeiroDoMes.plusMonths(1).atStartOfDay(FUSO).toOffsetDateTime();
        OffsetDateTime inicioAnterior = primeiroDoMes.minusMonths(1).atStartOfDay(FUSO).toOffsetDateTime();
        long noMes = denunciaRepository
                .countByDataRegistroGreaterThanEqualAndDataRegistroLessThan(inicioMes, inicioProximo);
        long noMesAnterior = denunciaRepository
                .countByDataRegistroGreaterThanEqualAndDataRegistroLessThan(inicioAnterior, inicioMes);
        Integer variacao = noMesAnterior == 0
                ? null
                : (int) Math.round((noMes - noMesAnterior) * 100.0 / noMesAnterior);

        // tempo médio entre o registro e a resolução
        OptionalDouble media = historicoRepository.datasAteOStatus(StatusDenuncia.RESOLVIDA).stream()
                .mapToDouble(p -> Duration.between((OffsetDateTime) p[0], (OffsetDateTime) p[1]).toMinutes() / 60.0)
                .average();
        Double tempoMedio = media.isPresent() ? Math.round(media.getAsDouble() * 10) / 10.0 : null;

        // bairro com mais ocorrências abertas
        List<Object[]> abertas = denunciaRepository
                .contarAbertasPorBairro(StatusDenuncia.RESOLVIDA, StatusDenuncia.CANCELADA);
        String bairroCritico = abertas.isEmpty() ? null : (String) abertas.get(0)[0];
        long abertasNele = abertas.isEmpty() ? 0 : (Long) abertas.get(0)[1];

        return new DashboardResumoResponse(total, noMes, variacao, resolvidas, taxa,
                tempoMedio, META_HORAS, bairroCritico, abertasNele);
    }

    @Transactional(readOnly = true)
    public List<String> bairros() {
        return denunciaRepository.listarBairros();
    }

    /** Tabela "Denúncias Recentes". */
    @Transactional(readOnly = true)
    public List<DenunciaGestaoResponse> recentes(StatusDenuncia status, String bairro, int limite) {
        return buscar(status, bairro, Math.min(Math.max(limite, 1), MAX_TABELA));
    }

    /** Pontos do mapa, com os mesmos filtros da tabela. */
    @Transactional(readOnly = true)
    public List<DenunciaGestaoResponse> mapa(StatusDenuncia status, String bairro) {
        return buscar(status, bairro, MAX_MAPA);
    }

    private List<DenunciaGestaoResponse> buscar(StatusDenuncia status, String bairro, int limite) {
        // filtros opcionais: o que vier vazio é ignorado
        Specification<Denuncia> filtros = (root, query, cb) -> {
            List<Predicate> condicoes = new ArrayList<>();
            if (status != null) {
                condicoes.add(cb.equal(root.get("status"), status));
            }
            if (bairro != null && !bairro.isBlank()) {
                condicoes.add(cb.equal(
                        cb.lower(root.get("localizacao").get("bairro")),
                        bairro.trim().toLowerCase()));
            }
            return cb.and(condicoes.toArray(new Predicate[0]));
        };

        return denunciaRepository.findAll(filtros,
                        PageRequest.of(0, limite, Sort.by(Sort.Direction.DESC, "dataRegistro", "id")))
                .getContent().stream()
                .map(DenunciaGestaoResponse::de)
                .toList();
    }
}
