package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.Notificacao;

import java.util.List;

public interface NotificacaoDAO extends GenericDAO<Notificacao, Integer> {

    List<Notificacao> listarPorDestinatario(Integer idUsuario);

    List<Notificacao> listarNaoLidas(Integer idUsuario);

    long contarNaoLidas(Integer idUsuario);
}
