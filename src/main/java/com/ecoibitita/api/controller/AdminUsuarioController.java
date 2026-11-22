package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.EditarUsuarioRequest;
import com.ecoibitita.api.dto.NovoUsuarioRequest;
import com.ecoibitita.api.dto.PaginaResponse;
import com.ecoibitita.api.dto.ResumoAdminResponse;
import com.ecoibitita.api.dto.UsuarioAdminResponse;
import com.ecoibitita.api.service.AdminUsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminUsuarioController {

    private final AdminUsuarioService service;

    public AdminUsuarioController(AdminUsuarioService service) {
        this.service = service;
    }

    @GetMapping("/resumo")
    public ResumoAdminResponse resumo() {
        return service.resumo();
    }

    @GetMapping("/usuarios")
    public PaginaResponse<UsuarioAdminResponse> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String perfil,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        return service.listar(busca, perfil, ativo, pagina, tamanho);
    }

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioAdminResponse criar(@Valid @RequestBody NovoUsuarioRequest dados,
                                      @AuthenticationPrincipal Jwt jwt) {
        return service.criar(dados, Integer.valueOf(jwt.getSubject()));
    }

    @PutMapping("/usuarios/{id}")
    public UsuarioAdminResponse editar(@PathVariable Integer id,
                                       @Valid @RequestBody EditarUsuarioRequest dados,
                                       @AuthenticationPrincipal Jwt jwt) {
        return service.editar(id, dados, Integer.valueOf(jwt.getSubject()));
    }
}