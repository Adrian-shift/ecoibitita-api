package com.ecoibitita.api.model.denuncia;

import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.model.usuario.Usuario;
import java.time.LocalDateTime;

public class HistoricoDenuncia {
    private Integer idHistorico;
    private LocalDateTime dataHora = LocalDateTime.now();
    private StatusDenuncia statusAnterior;
    private StatusDenuncia statusNovo;
    private String observacao;
    private Usuario responsavel;

    public Integer getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(Integer idHistorico) {
        this.idHistorico = idHistorico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusDenuncia getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusDenuncia statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusDenuncia getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusDenuncia statusNovo) {
        this.statusNovo = statusNovo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }
}
