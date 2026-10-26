package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.LoginResponse;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/gestao")
public class GestaoController {

    private final UsuarioRepository usuarioRepository;

    public GestaoController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // dados de quem está logado (também confirma que a conta ainda está ativa)
    @GetMapping("/me")
    public LoginResponse.UsuarioLogado me(@AuthenticationPrincipal Jwt jwt) {
        Usuario u = usuarioRepository.findById(Integer.valueOf(jwt.getSubject()))
                .filter(Usuario::isAtivo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sessão inválida"));
        return new LoginResponse.UsuarioLogado(u.getId(), u.getNome(), u.getEmail(), u.getPerfil().getNome());
    }
}