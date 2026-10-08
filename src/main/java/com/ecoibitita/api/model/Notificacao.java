package com.ecoibitita.api.model;

import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.usuario.Usuario;
import java.time.LocalDateTime;

public class Notificacao {
    private Integer idNotificacao;
    private String mensagem;
    private String tipo;
    private LocalDateTime dataEnvio = LocalDateTime.now();
    private boolean lida;
    private Usuario destinatario;
    private Denuncia denuncia;

    public void marcarComoLida() { this.lida = true; }

    public Integer getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(Integer idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }
}
