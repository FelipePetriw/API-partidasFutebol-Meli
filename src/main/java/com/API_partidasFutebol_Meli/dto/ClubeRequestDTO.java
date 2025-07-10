package com.API_partidasFutebol_Meli.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.processing.Pattern;

import java.time.LocalDate;

public record ClubeRequestDTO(
        @NotBlank @Size(min = 2)
        String nome,

        @NotBlank @Pattern(regexp = "AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO", message = "Estado inválido")
        String siglaEstado,

        @NotNull @PastOrPresent
        LocalDate dataCriacao
) {}
