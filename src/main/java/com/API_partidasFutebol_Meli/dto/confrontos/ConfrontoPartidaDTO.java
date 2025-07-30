package com.API_partidasFutebol_Meli.dto.confrontos;

import java.time.LocalDateTime;

public record ConfrontoPartidaDTO(
        LocalDateTime dataHora,
        String estadio,
        String mandante,
        String visitante,
        int golsMandante,
        int golsVisitante
) {}
