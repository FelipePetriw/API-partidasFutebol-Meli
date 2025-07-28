package com.API_partidasFutebol_Meli.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstadioRequestDTO(

        @NotBlank(message = "Nome não pode ser vazio.")
        @Size(min = 3, max = 100, message = "Deve ter entre 3 e 100 caracteres.")
        String nome
) {
}
