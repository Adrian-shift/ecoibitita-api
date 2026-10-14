package com.ecoibitita.dao;

import com.ecoibitita.model.denuncia.ObservacaoOperacional;

import java.util.List;

public interface ObservacaoOperacionalDAO extends GenericDAO<ObservacaoOperacional, Integer> {

    List<ObservacaoOperacional> listarPorDenuncia(Integer idDenuncia);
}
