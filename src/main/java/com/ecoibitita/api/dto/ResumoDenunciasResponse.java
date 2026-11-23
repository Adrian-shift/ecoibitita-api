package com.ecoibitita.api.dto;

import java.time.LocalDate;

public record ResumoDenunciasResponse(
        long total,
        long ultimos30Dias,
        long usuariosEnvolvidos,  // quem já alterou o status de alguma denúncia
        LocalDate periodoInicio,
        LocalDate periodoFim
) {}
