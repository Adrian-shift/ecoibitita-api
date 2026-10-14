package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.RecuperacaoSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Integer> {
    Optional<RecuperacaoSenha> findByTokenHashAndUsadoFalse(String tokenHash);
}