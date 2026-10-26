package com.ecoibitita.api.config;

import com.ecoibitita.api.model.usuario.Perfil;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.PerfilRepository;
import com.ecoibitita.api.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Cria o primeiro administrador quando o banco ainda não tem nenhum.
 * Depois do primeiro uso, remova ADMIN_SENHA das variáveis de ambiente.
 */
@Component
public class AdminInicial implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInicial.class);

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String nome;
    private final String senha;

    public AdminInicial(UsuarioRepository usuarioRepository,
                        PerfilRepository perfilRepository,
                        PasswordEncoder passwordEncoder,
                        @Value("${ADMIN_EMAIL:}") String email,
                        @Value("${ADMIN_NOME:Administrador}") String nome,
                        @Value("${ADMIN_SENHA:}") String senha) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByPerfilNome(Perfil.ADMINISTRADOR)) return;

        if (email.isBlank() || senha.isBlank()) {
            log.warn("Nenhum administrador cadastrado. Defina ADMIN_EMAIL, ADMIN_NOME e ADMIN_SENHA para criar o primeiro.");
            return;
        }
        if (senha.length() < 8) {
            log.warn("ADMIN_SENHA precisa ter pelo menos 8 caracteres. Administrador não criado.");
            return;
        }

        Perfil perfil = perfilRepository.findByNome(Perfil.ADMINISTRADOR)
                .orElseThrow(() -> new IllegalStateException("O perfil Administrador não existe no banco"));

        Usuario admin = new Usuario();
        admin.setPerfil(perfil);
        admin.setNome(nome.trim());
        admin.setEmail(email.trim().toLowerCase());
        admin.setSenhaHash(passwordEncoder.encode(senha));
        usuarioRepository.save(admin);

        log.info("Primeiro administrador criado: {}", admin.getEmail());
    }
}