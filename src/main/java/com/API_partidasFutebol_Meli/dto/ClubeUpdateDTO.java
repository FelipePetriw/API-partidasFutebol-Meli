package com.API_partidasFutebol_Meli.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

public record ClubeUpdateDTO(
        @NotBlank @Size(min = 2)
        String nome,

        @NotBlank @Pattern(
                regexp = "(?i)^(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)$",
                message = "Estado inválido")
        String siglaEstado,

        @NotNull @PastOrPresent @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataCriacao
) {}
