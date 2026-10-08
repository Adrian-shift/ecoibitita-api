package com.ecoibitita.model;

import com.ecoibitita.model.usuario.Usuario;
import java.time.LocalDateTime;

public class RegistroAuditoria {
    private Integer idRegistro;
    private LocalDateTime dataHora = LocalDateTime.now();
    private String operacao;
    private String entidade;
    private Integer idEntidade;
    private String descricao;
    private String enderecoIP;
    private Usuario usuario;

    public static RegistroAuditoria registrar(Usuario usuario, String operacao,
                                              String entidade, Integer idEntidade,
                                              String descricao, String enderecoIP) {
        RegistroAuditoria r = new RegistroAuditoria();
        r.usuario = usuario;
        r.operacao = operacao;
        r.entidade = entidade;
        r.idEntidade = idEntidade;
        r.descricao = descricao;
        r.enderecoIP = enderecoIP;
        return r;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public Integer getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(Integer idEntidade) {
        this.idEntidade = idEntidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEnderecoIP() {
        return enderecoIP;
    }

    public void setEnderecoIP(String enderecoIP) {
        this.enderecoIP = enderecoIP;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
