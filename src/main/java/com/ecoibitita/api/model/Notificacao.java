package com.ecoibitita.model;

import com.ecoibitita.model.denuncia.Denuncia;
import com.ecoibitita.model.usuario.Usuario;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario destinatario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "denuncia_id")
    private Denuncia denuncia;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    private boolean lida = false;

    @Column(name = "enviada_em")
    private OffsetDateTime enviadaEm = OffsetDateTime.now();

    // ---------- regras de negócio ----------

    public void marcarComoLida() {
        this.lida = true;
    }

    // ---------- getters e setters ----------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public OffsetDateTime getEnviadaEm() {
        return enviadaEm;
    }

    public void setEnviadaEm(OffsetDateTime enviadaEm) {
        this.enviadaEm = enviadaEm;
    }
}
