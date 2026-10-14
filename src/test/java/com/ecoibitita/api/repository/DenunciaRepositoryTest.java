package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.denuncia.CategoriaResiduo;
import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.denuncia.Localizacao;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // desfaz tudo no final, o banco não fica sujo
class DenunciaRepositoryTest {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private CategoriaResiduoRepository categoriaRepository;

    @Test
    void salvaEBuscaPorProtocoloEStatus() {
        CategoriaResiduo categoria = categoriaRepository.findByAtivoTrueOrderByNome().get(0);

        Localizacao loc = new Localizacao();
        loc.setLatitude(new BigDecimal("-11.54321000"));
        loc.setLongitude(new BigDecimal("-41.97654000"));
        loc.setBairro("Centro");

        Denuncia d = new Denuncia();
        d.setCategoria(categoria);
        d.setLocalizacao(loc);
        d.setDescricao("Teste de repositório");

        denunciaRepository.saveAndFlush(d);

        assertTrue(denunciaRepository.findByProtocolo(d.getProtocolo()).isPresent());
        assertFalse(denunciaRepository.findByStatus(StatusDenuncia.RECEBIDA).isEmpty());
        assertFalse(denunciaRepository.findByLocalizacaoBairro("Centro").isEmpty());
    }
}