package com.ecoibitita.api.dao;

import java.util.List;
import java.util.Optional;

/**
 * Contrato CRUD comum a todos os DAOs.
 *
 * @param <T>  tipo da entidade
 * @param <ID> tipo da chave primária
 */
public interface GenericDAO<T, ID> {

    T salvar(T entidade);

    T atualizar(T entidade);

    Optional<T> buscarPorId(ID id);

    List<T> listarTodos();

    void remover(T entidade);

    void removerPorId(ID id);
}
