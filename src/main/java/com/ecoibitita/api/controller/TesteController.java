package com.ecoibitita.api.controller;

import com.ecoibitita.api.model.usuario.Usuario;
import com.ecoibitita.api.repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TesteController {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "API EcoIbititá funcionando!");
        response.put("database", "Conectado");
        return response;
    }

    @GetMapping("/test-db")
    public Map<String, Object> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = denunciaRepository.count();
            response.put("status", "OK");
            response.put("message", "Conexão com banco funcionando!");
            response.put("denuncias_count", count);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Erro ao conectar com banco: " + e.getMessage());
        }
        return response;
    }
}
