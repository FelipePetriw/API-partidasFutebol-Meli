package com.API_partidasFutebol_Meli.dto.partida;

import java.time.LocalDateTime;

public record PartidaResponseDTO(
        Long id,
        String clubeMandante,
        String clubeVisitante,
        String estadio,
        LocalDateTime datahora,
        Integer golsMandante,
        Integer golsVisitante
) {
}
