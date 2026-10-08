package com.ecoibitita.model.denuncia;

import com.ecoibitita.model.enums.StatusDenuncia;
import com.ecoibitita.model.usuario.Cidadao;
import com.ecoibitita.model.usuario.Fiscal;
import com.ecoibitita.model.usuario.Usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Denuncia {
    private Integer idDenuncia;
    private String protocolo;
    private String descricao;
    private LocalDateTime dataHoraRegistro = LocalDateTime.now();
    private LocalDateTime dataHoraAtualizacao;
    private LocalDateTime dataHoraResolucao;
    private StatusDenuncia status = StatusDenuncia.RECEBIDA;
    private boolean anonima;

    private Cidadao cidadao;
    private Fiscal fiscal;
    private CategoriaResiduo categoria;
    private Localizacao localizacao;
    private List<Imagem> imagens = new ArrayList<>();
    private List<ObservacaoOperacional> observacoes = new ArrayList<>();
    private List<HistoricoDenuncia> historico = new ArrayList<>();

    public Denuncia() {
        this.protocolo = "ECO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void alterarStatus(StatusDenuncia novoStatus, Usuario responsavel, String observacao) {
        HistoricoDenuncia h = new HistoricoDenuncia();
        h.setStatusAnterior(this.status);
        h.setStatusNovo(novoStatus);
        h.setResponsavel(responsavel);
        h.setObservacao(observacao);
        this.historico.add(h);

        this.status = novoStatus;
        this.dataHoraAtualizacao = LocalDateTime.now();
    }

    public void resolver(Fiscal fiscal) {
        alterarStatus(StatusDenuncia.RESOLVIDA, fiscal, "Limpeza concluída");
        this.dataHoraResolucao = LocalDateTime.now();
    }

    public void cancelar(Usuario responsavel, String motivo) {
        alterarStatus(StatusDenuncia.CANCELADA, responsavel, motivo);
    }

    public void adicionarImagem(Imagem imagem) {
        if (!imagem.validarArquivo()) {
            throw new IllegalArgumentException("Arquivo de imagem inválido");
        }
        this.imagens.add(imagem);
    }

    public void adicionarObservacao(ObservacaoOperacional obs) {
        this.observacoes.add(obs);
    }

    public Integer getIdDenuncia() {
        return idDenuncia;
    }

    public void setIdDenuncia(Integer idDenuncia) {
        this.idDenuncia = idDenuncia;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }

    public LocalDateTime getDataHoraAtualizacao() {
        return dataHoraAtualizacao;
    }

    public void setDataHoraAtualizacao(LocalDateTime dataHoraAtualizacao) {
        this.dataHoraAtualizacao = dataHoraAtualizacao;
    }

    public LocalDateTime getDataHoraResolucao() {
        return dataHoraResolucao;
    }

    public void setDataHoraResolucao(LocalDateTime dataHoraResolucao) {
        this.dataHoraResolucao = dataHoraResolucao;
    }

    public StatusDenuncia getStatus() {
        return status;
    }

    public void setStatus(StatusDenuncia status) {
        this.status = status;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public Cidadao getCidadao() {
        return cidadao;
    }

    public void setCidadao(Cidadao cidadao) {
        this.cidadao = cidadao;
    }

    public Fiscal getFiscal() {
        return fiscal;
    }

    public void setFiscal(Fiscal fiscal) {
        this.fiscal = fiscal;
    }

    public CategoriaResiduo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaResiduo categoria) {
        this.categoria = categoria;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }

    public List<ObservacaoOperacional> getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(List<ObservacaoOperacional> observacoes) {
        this.observacoes = observacoes;
    }

    public List<HistoricoDenuncia> getHistorico() {
        return historico;
    }

    public void setHistorico(List<HistoricoDenuncia> historico) {
        this.historico = historico;
    }
}
