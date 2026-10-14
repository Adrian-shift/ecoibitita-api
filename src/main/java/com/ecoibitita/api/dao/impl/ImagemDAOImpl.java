package com.ecoibitita.api.dao.impl;

import com.ecoibitita.api.dao.ImagemDAO;
import com.ecoibitita.api.model.denuncia.Imagem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ImagemDAOImpl extends GenericDAOImpl<Imagem, Integer> implements ImagemDAO {

    public ImagemDAOImpl() {
        super(Imagem.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Imagem> listarPorDenuncia(Integer idDenuncia) {
        return em.createQuery(
                        "select i from Imagem i where i.denuncia.id = :id order by i.criadoEm",
                        Imagem.class)
                .setParameter("id", idDenuncia)
                .getResultList();
    }
}
