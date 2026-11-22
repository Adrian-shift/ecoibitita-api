package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.PaginaResponse;
import com.ecoibitita.api.dto.ResumoAdminResponse;
import com.ecoibitita.api.dto.UsuarioAdminResponse;
import com.ecoibitita.api.service.AdminUsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
