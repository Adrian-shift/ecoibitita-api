package com.ecoibitita.api.repository;

import com.ecoibitita.api.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByPerfilNomeAndAtivoTrue(String nomePerfil);
}
