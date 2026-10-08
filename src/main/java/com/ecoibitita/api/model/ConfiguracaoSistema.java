package com.ecoibitita.api.model;

public class ConfiguracaoSistema {
    private Integer idConfiguracao;
    private String nome;
    private String valor;
    private String descricao;
    private boolean ativo = true;

    public boolean valorComoBoolean() {
        return Boolean.parseBoolean(valor);
    }

    public int valorComoInteiro() {
        return Integer.parseInt(valor);
    }

    public Integer getIdConfiguracao() {
        return idConfiguracao;
    }

    public void setIdConfiguracao(Integer idConfiguracao) {
        this.idConfiguracao = idConfiguracao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
