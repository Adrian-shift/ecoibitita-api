package com.ecoibitita.model.usuario;

import jakarta.persistence.*;

@Entity
@Table(name = "perfis")
public class Perfil {

    public static final String ADMINISTRADOR = "Administrador";
    public static final String FISCAL = "Fiscal";
    public static final String CIDADAO = "Cidadão";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String nome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
