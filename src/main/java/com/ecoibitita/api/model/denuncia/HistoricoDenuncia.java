package com.ecoibitita.api.model.denuncia;

import com.ecoibitita.api.model.converter.StatusDenunciaConverter;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.model.usuario.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "historico_status")
public class HistoricoDenuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "denuncia_id")
    private Denuncia denuncia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Convert(converter = StatusDenunciaConverter.class)
    @Column(name = "status_anterior", length = 30)
    private StatusDenuncia statusAnterior;

    @Convert(converter = StatusDenunciaConverter.class)
    @Column(name = "status_novo", nullable = false, length = 30)
    private StatusDenuncia statusNovo;

    @Column(length = 255)
    private String observacao;

    @Column(name = "alterado_em")
    private OffsetDateTime alteradoEm = OffsetDateTime.now();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Denuncia getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public OffsetDateTime getAlteradoEm() {
        return alteradoEm;
    }

    public void setAlteradoEm(OffsetDateTime alteradoEm) {
        this.alteradoEm = alteradoEm;
    }
}
