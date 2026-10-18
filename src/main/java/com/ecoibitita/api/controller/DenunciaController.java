package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.CategoriaResponse;
import com.ecoibitita.api.dto.DenunciaResponse;
import com.ecoibitita.api.dto.NovaDenunciaRequest;
import com.ecoibitita.api.repository.CategoriaResiduoRepository;
import com.ecoibitita.api.service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DenunciaController {

    private final DenunciaService denunciaService;
    private final CategoriaResiduoRepository categoriaRepository;

    public DenunciaController(DenunciaService denunciaService,
                              CategoriaResiduoRepository categoriaRepository) {
        this.denunciaService = denunciaService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/categorias")
    public List<CategoriaResponse> listarCategorias() {
        return categoriaRepository.findByAtivoTrueOrderByNome().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNome()))
                .toList();
    }

    @PostMapping(value = "/denuncias", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public DenunciaResponse registrar(@Valid @ModelAttribute NovaDenunciaRequest dados,
                                      @RequestParam("fotos") List<MultipartFile> fotos) {
        return DenunciaResponse.de(denunciaService.registrar(dados, fotos));
    }
}