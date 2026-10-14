package com.ecoibitita.dao;

import com.ecoibitita.model.denuncia.CategoriaResiduo;

import java.util.List;
import java.util.Optional;

public interface CategoriaResiduoDAO extends GenericDAO<CategoriaResiduo, Integer> {

    Optional<CategoriaResiduo> buscarPorNome(String nome);

    List<CategoriaResiduo> listarAtivas();
}
