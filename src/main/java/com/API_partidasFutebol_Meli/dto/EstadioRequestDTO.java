package com.API_partidasFutebol_Meli.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstadioRequestDTO(

        @NotBlank
        @Size(min = 3)
        String nome
) {
}
