package com.ecoibitita.model.denuncia;

import com.ecoibitita.model.converter.StatusDenunciaConverter;
import com.ecoibitita.model.enums.StatusDenuncia;
import com.ecoibitita.model.usuario.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "denuncias")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String protocolo;

    // null = denúncia anônima
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario cidadao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private CategoriaResiduo categoria;

    @Embedded
    private Localizacao localizacao;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Convert(converter = StatusDenunciaConverter.class)
    @Column(nullable = false, length = 30)
    private StatusDenuncia status = StatusDenuncia.RECEBIDA;

    @Column(name = "data_registro")
    private OffsetDateTime dataRegistro = OffsetDateTime.now();

    @Column(name = "data_atualizacao")
    private OffsetDateTime dataAtualizacao = OffsetDateTime.now();

    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagem> imagens = new ArrayList<>();

    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObservacaoOperacional> observacoes = new ArrayList<>();

    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("alteradoEm ASC")
    private List<HistoricoDenuncia> historico = new ArrayList<>();

    public Denuncia() {
        this.protocolo = "ECO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ---------- regras de negócio ----------

    public void alterarStatus(StatusDenuncia novoStatus, Usuario responsavel, String observacao) {
        HistoricoDenuncia h = new HistoricoDenuncia();
        h.setDenuncia(this);
        h.setStatusAnterior(this.status);
        h.setStatusNovo(novoStatus);
        h.setUsuario(responsavel);
        h.setObservacao(observacao);
        this.historico.add(h);

        this.status = novoStatus;
        this.dataAtualizacao = OffsetDateTime.now();
    }

    public void resolver(Usuario fiscal) {
        alterarStatus(StatusDenuncia.RESOLVIDA, fiscal, "Limpeza concluída");
    }

    public void cancelar(Usuario responsavel, String motivo) {
        alterarStatus(StatusDenuncia.CANCELADA, responsavel, motivo);
    }

    public void adicionarImagem(Imagem imagem) {
        imagem.setDenuncia(this);
        this.imagens.add(imagem);
    }

    public void adicionarObservacao(ObservacaoOperacional observacao) {
        observacao.setDenuncia(this);
        this.observacoes.add(observacao);
    }

    // ---------- valores derivados (não são colunas do banco) ----------

    @Transient
    public boolean isAnonima() {
        return cidadao == null;
    }

    @Transient
    public OffsetDateTime getDataResolucao() {
        return historico.stream()
                .filter(h -> h.getStatusNovo() == StatusDenuncia.RESOLVIDA)
                .map(HistoricoDenuncia::getAlteradoEm)
                .reduce((primeiro, ultimo) -> ultimo)
                .orElse(null);
    }

    @Transient
    public Usuario getFiscalResponsavel() {
        return historico.stream()
                .filter(h -> h.getStatusNovo() == StatusDenuncia.EM_ATENDIMENTO)
                .map(HistoricoDenuncia::getUsuario)
                .reduce((primeiro, ultimo) -> ultimo)
                .orElse(null);
    }

    // ---------- getters e setters ----------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public Usuario getCidadao() {
        return cidadao;
    }

    public void setCidadao(Usuario cidadao) {
        this.cidadao = cidadao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusDenuncia getStatus() {
        return status;
    }

    public void setStatus(StatusDenuncia status) {
        this.status = status;
    }

    public OffsetDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(OffsetDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public OffsetDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(OffsetDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public List<ObservacaoOperacional> getObservacoes() {
        return observacoes;
    }

    public List<HistoricoDenuncia> getHistorico() {
        return historico;
    }
}
