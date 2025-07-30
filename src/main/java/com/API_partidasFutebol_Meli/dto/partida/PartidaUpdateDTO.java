package com.API_partidasFutebol_Meli.dto.partida;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record PartidaUpdateDTO(
        @NotNull
        Long clubeMandanteId,

        @NotNull
        Long clubeVisitanteId,

        @NotNull
        Long estadioId,

        @NotNull
        @PastOrPresent
        LocalDateTime dataHora,

        @NotNull
        @Min(0)
        Integer golsMandante,

        @NotNull
        @Min(0)
        Integer golsVisitante
) {}
