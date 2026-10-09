package com.ecoibitita.model.enums;

public enum TipoImagem {
    OCORRENCIA("Ocorrência"),
    COMPROBATORIA("Comprobatória");

    private final String descricao;

    TipoImagem(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoImagem deDescricao(String descricao) {
        for (TipoImagem t : values()) {
            if (t.descricao.equals(descricao)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de imagem desconhecido: " + descricao);
    }
}
