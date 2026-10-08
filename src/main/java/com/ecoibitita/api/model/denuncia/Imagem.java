package com.ecoibitita.api.model.denuncia;

import com.ecoibitita.api.model.enums.TipoImagem;
import java.time.LocalDateTime;

public class Imagem {
    private Integer idImagem;
    private String nomeArquivo;
    private String caminhoArquivo;
    private String tipoArquivo;
    private long tamanho;
    private LocalDateTime dataUpload = LocalDateTime.now();
    private TipoImagem tipoImagem;

    public boolean validarArquivo() {
        return tipoArquivo != null
                && (tipoArquivo.equals("image/jpeg") || tipoArquivo.equals("image/png"))
                && tamanho > 0 && tamanho <= 5 * 1024 * 1024;
    }

    public Integer getIdImagem() {
        return idImagem;
    }

    public void setIdImagem(Integer idImagem) {
        this.idImagem = idImagem;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(long tamanho) {
        this.tamanho = tamanho;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }

    public TipoImagem getTipoImagem() {
        return tipoImagem;
    }

    public void setTipoImagem(TipoImagem tipoImagem) {
        this.tipoImagem = tipoImagem;
    }
}
