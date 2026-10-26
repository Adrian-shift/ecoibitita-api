package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByPerfilNome(String nome);
}