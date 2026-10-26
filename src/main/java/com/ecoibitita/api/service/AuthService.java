package com.ecoibitita.api.service;

import com.ecoibitita.api.dto.LoginRequest;
import com.ecoibitita.api.dto.LoginResponse;
import com.ecoibitita.api.model.usuario.Perfil;
import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    // a mesma mensagem para qualquer falha, para não revelar se o e-mail existe
    private static final String ERRO_LOGIN = "E-mail ou senha inválidos";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long expiracaoHoras;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder,
                       @Value("${jwt.expiracao-horas:8}") long expiracaoHoras) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.expiracaoHoras = expiracaoHoras;
    }

    public LoginResponse entrar(LoginRequest req) {
        Usuario u = usuarioRepository.findByEmailIgnoreCase(req.email().trim())
                .filter(Usuario::isAtivo)
                .filter(x -> x.temPerfil(Perfil.ADMINISTRADOR) || x.temPerfil(Perfil.FISCAL))
                .filter(x -> passwordEncoder.matches(req.senha(), x.getSenhaHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERRO_LOGIN));

        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("ecoibitita-api")
                .subject(String.valueOf(u.getId()))
                .issuedAt(agora)
                .expiresAt(agora.plus(expiracaoHoras, ChronoUnit.HOURS))
                .claim("perfil", u.getPerfil().getNome())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new LoginResponse(
                token, "Bearer", claims.getExpiresAt(),
                new LoginResponse.UsuarioLogado(u.getId(), u.getNome(), u.getEmail(), u.getPerfil().getNome()));
    }
}