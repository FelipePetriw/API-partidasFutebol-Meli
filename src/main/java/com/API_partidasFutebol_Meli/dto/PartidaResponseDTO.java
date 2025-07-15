package com.API_partidasFutebol_Meli.dto;

public record PartidaResponseDTO(
        Long id,
        String clubeMandante,
        String clubeVisitante,
        String estadio,
        java.time.LocalDateTime datahora,
        Integer golsMandante,
        Integer golsVisitante
) {
}
