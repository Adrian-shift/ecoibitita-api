package com.ecoibitita.dao;

import com.ecoibitita.model.denuncia.Imagem;

import java.util.List;

public interface ImagemDAO extends GenericDAO<Imagem, Integer> {

    List<Imagem> listarPorDenuncia(Integer idDenuncia);
}
