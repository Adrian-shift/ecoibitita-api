package com.ecoibitita.api.dto;

public record ResumoAdminResponse(
        long totalUsuarios,
        long administradores,
        long fiscais,
        long usuariosAtivos,
        long novosNoMes,
        long categoriasAtivas,
        long logsHoje
) {}