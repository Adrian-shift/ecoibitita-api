package com.ecoibitita.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "configuracoes_sistema")
public class ConfiguracaoSistema {

    public static final String DENUNCIA_ANONIMA_HABILITADA = "DENUNCIA_ANONIMA_HABILITADA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String chave;

    @Column(nullable = false)
    private String valor;

    @Column(name = "atualizado_em")
    private OffsetDateTime atualizadoEm = OffsetDateTime.now();

    // ---------- regras de negócio ----------

    public boolean valorComoBoolean() {
        return Boolean.parseBoolean(valor);
    }

    public void alterarValor(String novoValor) {
        this.valor = novoValor;
        this.atualizadoEm = OffsetDateTime.now();
    }

    // ---------- getters e setters ----------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(OffsetDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}
