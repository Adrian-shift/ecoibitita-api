package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.denuncia.ObservacaoOperacional;

import java.util.List;

public interface ObservacaoOperacionalDAO extends GenericDAO<ObservacaoOperacional, Integer> {

    List<ObservacaoOperacional> listarPorDenuncia(Integer idDenuncia);
}
