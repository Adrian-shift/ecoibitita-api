package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.denuncia.CategoriaResiduo;

import java.util.List;
import java.util.Optional;

public interface CategoriaResiduoDAO extends GenericDAO<CategoriaResiduo, Integer> {

    Optional<CategoriaResiduo> buscarPorNome(String nome);

    List<CategoriaResiduo> listarAtivas();
}
