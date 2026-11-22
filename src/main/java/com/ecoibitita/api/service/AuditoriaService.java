package com.ecoibitita.api.service;

import com.ecoibitita.api.model.RegistroAuditoria;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.RegistroAuditoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaService {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaService.class);

    private final RegistroAuditoriaRepository repository;

    public AuditoriaService(RegistroAuditoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(Usuario autor, String acao, String entidade, Integer entidadeId, String detalhes) {
        try {
            repository.save(RegistroAuditoria.registrar(autor, acao, entidade, entidadeId, detalhes));
        } catch (Exception e) {
            log.warn("Não foi possível registrar a auditoria ({}): {}", acao, e.getMessage());
        }
    }
}
