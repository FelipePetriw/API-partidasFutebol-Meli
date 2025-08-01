package com.API_partidasFutebol_Meli.dto.clube;

import java.time.LocalDate;

public record ClubeResponseDTO(
        Long id,
        String nome,
        String siglaEstado,
        LocalDate dataCriacao,
        Boolean ativo
) {
}
