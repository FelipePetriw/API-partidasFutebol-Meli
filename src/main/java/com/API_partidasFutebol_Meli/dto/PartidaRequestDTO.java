package com.API_partidasFutebol_Meli.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PartidaRequestDTO(
        @NotNull
        Long clubeMandante,

        @NotNull
        Long clubeVisitante,

        @NotNull
        Long estadioId,

        @NotNull
        @PastOrPresent
        LocalDate dataHora,

        @NotNull
        @Min(0)
        Integer golsMandante,

        @NotNull
        @Min(0)
        Integer golsVisitante
) {}
