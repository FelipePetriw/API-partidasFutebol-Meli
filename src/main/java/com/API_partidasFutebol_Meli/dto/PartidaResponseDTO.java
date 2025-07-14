package com.API_partidasFutebol_Meli.dto;

import java.time.LocalDate;

public record PartidaResponseDTO(
        Long id,
        String clubeMandante,
        String clubeVisitante,
        String estadio,
        LocalDate datahora,
        Integer golsMandante,
        Integer golsVisitante
) {
}
