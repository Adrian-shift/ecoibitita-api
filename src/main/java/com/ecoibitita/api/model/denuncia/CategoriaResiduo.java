package com.ecoibitita.api.model.denuncia;

import java.time.LocalDateTime;

public class CategoriaResiduo {
    private Integer idCategoria;
    private String nome;
    private String descricao;
    private boolean ativo = true;
    private LocalDateTime dataCadastro = LocalDateTime.now();

    public void ativar()    { this.ativo = true; }
    public void desativar() { this.ativo = false; }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
