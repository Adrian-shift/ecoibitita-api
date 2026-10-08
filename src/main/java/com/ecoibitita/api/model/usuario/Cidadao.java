package com.ecoibitita.api.model.usuario;

public class Cidadao extends Usuario {
    private String cpf;
    private boolean aceitaIdentificacao;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isAceitaIdentificacao() {
        return aceitaIdentificacao;
    }

    public void setAceitaIdentificacao(boolean aceitaIdentificacao) {
        this.aceitaIdentificacao = aceitaIdentificacao;
    }
}
