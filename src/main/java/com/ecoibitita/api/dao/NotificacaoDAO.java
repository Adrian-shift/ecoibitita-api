package com.ecoibitita.dao;

import com.ecoibitita.model.Notificacao;

import java.util.List;

public interface NotificacaoDAO extends GenericDAO<Notificacao, Integer> {

    List<Notificacao> listarPorDestinatario(Integer idUsuario);

    List<Notificacao> listarNaoLidas(Integer idUsuario);

    long contarNaoLidas(Integer idUsuario);
}
