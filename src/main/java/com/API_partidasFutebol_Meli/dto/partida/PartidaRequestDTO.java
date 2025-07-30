package com.API_partidasFutebol_Meli.dto.partida;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record PartidaRequestDTO(
        @NotNull(message = "ID do clube mandante não pode ser nulo")
        Long clubeMandanteId,

        @NotNull(message = "ID do clube visitante não pode ser nulo")
        Long clubeVisitanteId,

        @NotNull(message = "ID do estádio não pode ser nulo")
        Long estadioId,

        @NotNull(message = "Data e Hora não podem ser nulos")
        @PastOrPresent
        LocalDateTime dataHora,

        @NotNull
        @Min(0)
        Integer golsMandante,

        @NotNull
        @Min(0)
        Integer golsVisitante
) {}
