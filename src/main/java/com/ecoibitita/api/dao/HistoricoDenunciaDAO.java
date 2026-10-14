package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.denuncia.HistoricoDenuncia;

import java.util.List;

public interface HistoricoDenunciaDAO extends GenericDAO<HistoricoDenuncia, Integer> {

    List<HistoricoDenuncia> listarPorDenuncia(Integer idDenuncia);
}
