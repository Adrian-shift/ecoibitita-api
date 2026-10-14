package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    List<Notificacao> findByDestinatarioIdOrderByEnviadaEmDesc(Integer usuarioId);
    long countByDestinatarioIdAndLidaFalse(Integer usuarioId);
}