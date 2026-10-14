package com.ecoibitita.dao;

import com.ecoibitita.model.denuncia.HistoricoDenuncia;

import java.util.List;

public interface HistoricoDenunciaDAO extends GenericDAO<HistoricoDenuncia, Integer> {

    List<HistoricoDenuncia> listarPorDenuncia(Integer idDenuncia);
}
