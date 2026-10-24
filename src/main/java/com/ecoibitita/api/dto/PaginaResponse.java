package com.ecoibitita.api.dto;

import java.util.List;

public record PaginaResponse<T>(
        List<T> itens,
        int pagina,
        int tamanho,
        long total,
        boolean ultima
) {}
