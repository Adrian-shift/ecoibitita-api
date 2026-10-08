package com.ecoibitita.model.denuncia;

import java.time.LocalDateTime;
import com.ecoibitita.model.usuario.Fiscal;


public class ObservacaoOperacional {
    private Integer idObservacao;
    private String texto;
    private LocalDateTime dataHora = LocalDateTime.now();
    private Fiscal fiscal;

    public Integer getIdObservacao() {
        return idObservacao;
    }

    public void setIdObservacao(Integer idObservacao) {
        this.idObservacao = idObservacao;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Fiscal getFiscal() {
        return fiscal;
    }

    public void setFiscal(Fiscal fiscal) {
        this.fiscal = fiscal;
    }
}
