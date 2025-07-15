package com.API_partidasFutebol_Meli.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadioResponseDTO(

        Long id,
        String nome
) {
}
