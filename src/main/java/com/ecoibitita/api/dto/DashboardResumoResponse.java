package com.ecoibitita.api.dto;

public record DashboardResumoResponse(
        long volume,                       // total de denúncias
        long volumeNoMes,                  // denúncias do mês atual
        Integer variacaoMensalPercentual,  // mês atual x mês anterior (null se o anterior foi 0)
        long resolvidas,
        int taxaResolucaoPercentual,
        Double tempoMedioHoras,            // do registro até "Resolvida" (null se nada foi resolvido)
        int metaHoras,
        String bairroCritico,              // bairro com mais ocorrências abertas (null se não há)
        long bairroCriticoAbertas
) {}
