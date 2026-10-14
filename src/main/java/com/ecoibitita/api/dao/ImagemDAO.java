package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.denuncia.Imagem;

import java.util.List;

public interface ImagemDAO extends GenericDAO<Imagem, Integer> {

    List<Imagem> listarPorDenuncia(Integer idDenuncia);
}
