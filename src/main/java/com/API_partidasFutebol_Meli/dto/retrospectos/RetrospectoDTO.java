package com.API_partidasFutebol_Meli.dto.retrospectos;

public record RetrospectoDTO(
        Long cludeId,
        String clubeNome,
        int vitorias,
        int empates,
        int derrotas,
        int golsFeitos,
        int golsSofridos
) {
}
