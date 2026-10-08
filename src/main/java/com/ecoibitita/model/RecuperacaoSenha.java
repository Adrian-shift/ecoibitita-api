package com.ecoibitita.model;

import com.ecoibitita.model.usuario.Usuario;
import java.time.LocalDateTime;
import java.util.UUID;

public class RecuperacaoSenha {
    private Integer idRecuperacao;
    private String token;
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    private LocalDateTime dataExpiracao;
    private boolean utilizado = false;
    private Usuario usuario;

    public RecuperacaoSenha() {
    }

    public RecuperacaoSenha(Usuario usuario) {
        this.usuario = usuario;
        gerarToken();
    }

    public void gerarToken() {
        this.token = UUID.randomUUID().toString();
        this.dataSolicitacao = LocalDateTime.now();
        this.dataExpiracao = this.dataSolicitacao.plusMinutes(30);
        this.utilizado = false;
    }

    public boolean tokenValido() {
        return !utilizado && LocalDateTime.now().isBefore(dataExpiracao);
    }

    public void marcarComoUtilizado() {
        this.utilizado = true;
    }

    public Integer getIdRecuperacao() {
        return idRecuperacao;
    }

    public void setIdRecuperacao(Integer idRecuperacao) {
        this.idRecuperacao = idRecuperacao;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
