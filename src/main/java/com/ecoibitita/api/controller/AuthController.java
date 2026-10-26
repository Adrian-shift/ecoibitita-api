package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.LoginRequest;
import com.ecoibitita.api.dto.LoginResponse;
import com.ecoibitita.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest dados) {
        return authService.entrar(dados);
    }
}
