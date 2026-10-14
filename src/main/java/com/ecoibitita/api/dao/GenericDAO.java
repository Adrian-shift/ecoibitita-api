package com.ecoibitita.api.dao;

import java.util.List;
import java.util.Optional;


// <T>  tipo entidade
// <ID> tipo pk
public interface GenericDAO<T, ID> {

    T salvar(T entidade);

    T atualizar(T entidade);

    Optional<T> buscarPorId(ID id);

    List<T> listarTodos();

    void remover(T entidade);

    void removerPorId(ID id);
}
