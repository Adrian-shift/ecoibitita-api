package com.ecoibitita.dao;

import com.ecoibitita.model.RegistroAuditoria;

import java.time.OffsetDateTime;
import java.util.List;

public interface RegistroAuditoriaDAO extends GenericDAO<RegistroAuditoria, Integer> {

    List<RegistroAuditoria> listarPorUsuario(Integer idUsuario);

    List<RegistroAuditoria> listarPorEntidade(String entidade, Integer idEntidade);

    List<RegistroAuditoria> listarPorPeriodo(OffsetDateTime inicio, OffsetDateTime fim);
}
