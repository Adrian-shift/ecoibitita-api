package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.PerfilDAO;
import com.ecoibitita.model.usuario.Perfil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class PerfilDAOImpl extends GenericDAOImpl<Perfil, Integer> implements PerfilDAO {

    public PerfilDAOImpl() {
        super(Perfil.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Perfil> buscarPorNome(String nome) {
        return em.createQuery("select p from Perfil p where p.nome = :nome", Perfil.class)
                .setParameter("nome", nome)
                .getResultStream()
                .findFirst();
    }
}
