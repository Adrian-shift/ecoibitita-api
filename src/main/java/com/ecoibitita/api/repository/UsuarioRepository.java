package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>,
        JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByPerfilNome(String nome);

    long countByPerfilNome(String nome);

    long countByPerfilNomeIn(Collection<String> nomes);

    long countByAtivoTrueAndPerfilNomeIn(Collection<String> nomes);

    long countByPerfilNomeInAndCriadoEmGreaterThanEqual(Collection<String> nomes, OffsetDateTime desde);
}