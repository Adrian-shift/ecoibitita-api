package com.ecoibitita.model;

import com.ecoibitita.model.usuario.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "logs_auditoria")
public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String acao;                 // ex.: "ALTERAR_STATUS", "LOGIN"

    @Column(length = 50)
    private String entidade;             // ex.: "Denuncia"

    @Column(name = "entidade_id")
    private Integer entidadeId;

    @Column(columnDefinition = "TEXT")
    private String detalhes;             // se precisar do IP, escreva aqui

    @Column(name = "data_hora")
    private OffsetDateTime dataHora = OffsetDateTime.now();

    // ---------- regras de negócio ----------

    public static RegistroAuditoria registrar(Usuario usuario, String acao,
                                              String entidade, Integer entidadeId,
                                              String detalhes) {
        RegistroAuditoria r = new RegistroAuditoria();
        r.usuario = usuario;
        r.acao = acao;
        r.entidade = entidade;
        r.entidadeId = entidadeId;
        r.detalhes = detalhes;
        return r;
    }

    // ---------- getters e setters ----------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public Integer getEntidadeId() {
        return entidadeId;
    }

    public void setEntidadeId(Integer entidadeId) {
        this.entidadeId = entidadeId;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
