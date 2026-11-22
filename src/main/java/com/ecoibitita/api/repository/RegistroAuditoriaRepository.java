package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.RegistroAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.OffsetDateTime;

public interface RegistroAuditoriaRepository extends JpaRepository<RegistroAuditoria, Integer> {
    List<RegistroAuditoria> findByEntidadeAndEntidadeIdOrderByDataHoraDesc(String entidade, Integer entidadeId);
    List<RegistroAuditoria> findByUsuarioIdOrderByDataHoraDesc(Integer usuarioId);
    long countByDataHoraGreaterThanEqualAndAcaoNot(OffsetDateTime desde, String acao);
}
