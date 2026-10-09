package com.ecoibitita.model.enums;

public enum StatusDenuncia {
    RECEBIDA("Recebida"),
    EM_ANALISE("Em análise"),
    EM_ATENDIMENTO("Em atendimento"),
    RESOLVIDA("Resolvida"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusDenuncia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusDenuncia deDescricao(String descricao) {
        for (StatusDenuncia s : values()) {
            if (s.descricao.equals(descricao)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + descricao);
    }
}
