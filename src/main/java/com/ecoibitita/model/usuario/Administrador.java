package com.ecoibitita.model.usuario;

public class Administrador extends Usuario {
    private String cargo;

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
