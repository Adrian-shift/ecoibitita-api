package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.DashboardResumoResponse;
import com.ecoibitita.api.dto.DenunciaGestaoResponse;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// /api/gestao/** já é liberado para Administrador e Fiscal no SecurityConfig
@RestController
@RequestMapping("/api/gestao/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/resumo")
    public DashboardResumoResponse resumo() {
        return service.resumo();
    }

    @GetMapping("/bairros")
    public List<String> bairros() {
        return service.bairros();
    }

    @GetMapping("/denuncias")
    public List<DenunciaGestaoResponse> recentes(
            @RequestParam(required = false) StatusDenuncia status,
            @RequestParam(required = false) String bairro,
            @RequestParam(defaultValue = "10") int limite) {
        return service.recentes(status, bairro, limite);
    }

    @GetMapping("/mapa")
    public List<DenunciaGestaoResponse> mapa(
            @RequestParam(required = false) StatusDenuncia status,
            @RequestParam(required = false) String bairro) {
        return service.mapa(status, bairro);
    }
}