package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.AlterarStatusRequest;
import com.ecoibitita.api.dto.DenunciaGestaoDetalheResponse;
import com.ecoibitita.api.dto.DenunciaGestaoResponse;
import com.ecoibitita.api.dto.PaginaResponse;
import com.ecoibitita.api.dto.ResumoDenunciasResponse;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import com.ecoibitita.api.service.GestaoDenunciaService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

// /api/gestao/** já é liberado para Administrador e Fiscal no SecurityConfig
@RestController
@RequestMapping("/api/gestao/denuncias")
public class GestaoDenunciaController {

    private final GestaoDenunciaService service;

    public GestaoDenunciaController(GestaoDenunciaService service) {
        this.service = service;
    }

    @GetMapping("/resumo")
    public ResumoDenunciasResponse resumo() {
        return service.resumo();
    }

    @GetMapping
    public PaginaResponse<DenunciaGestaoResponse> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusDenuncia status,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) String bairro,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        return service.listar(busca, status, categoriaId, bairro, pagina, tamanho);
    }

    @GetMapping("/{id}")
    public DenunciaGestaoDetalheResponse detalhe(@PathVariable Integer id) {
        return service.detalhe(id);
    }

    @PatchMapping("/{id}/status")
    public DenunciaGestaoDetalheResponse alterarStatus(@PathVariable Integer id,
                                                       @Valid @RequestBody AlterarStatusRequest dados,
                                                       @AuthenticationPrincipal Jwt jwt) {
        return service.alterarStatus(id, dados, Integer.valueOf(jwt.getSubject()));
    }
}