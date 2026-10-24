package com.ecoibitita.api.controller;

import com.ecoibitita.api.dto.*;
import com.ecoibitita.api.repository.CategoriaResiduoRepository;
import com.ecoibitita.api.service.ConsultaDenunciaService;
import com.ecoibitita.api.service.DenunciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ecoibitita.api.model.enums.StatusDenuncia;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DenunciaController {

    private final DenunciaService denunciaService;
    private final ConsultaDenunciaService consultaService;
    private final CategoriaResiduoRepository categoriaRepository;

    public DenunciaController(DenunciaService denunciaService,
                              ConsultaDenunciaService consultaService,
                              CategoriaResiduoRepository categoriaRepository) {
        this.denunciaService = denunciaService;
        this.consultaService = consultaService;
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

    @GetMapping("/denuncias/{protocolo}")
    public DenunciaDetalheResponse buscar(@PathVariable String protocolo) {
        return consultaService.buscarPorProtocolo(protocolo);
    }

    @GetMapping("/denuncias")
    public List<DenunciaResumoResponse> resumos(@RequestParam List<String> protocolos) {
        return consultaService.resumos(protocolos);
    }

    @GetMapping("/denuncias/publicas")
    public PaginaResponse<DenunciaPublicaResponse> listarPublicas(
            @RequestParam(required = false) StatusDenuncia status,
            @RequestParam(required = false) String bairro,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho) {
        return consultaService.listarPublicas(status, bairro, pagina, tamanho);
    }
}