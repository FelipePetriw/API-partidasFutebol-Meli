package com.API_partidasFutebol_Meli.dto;

public record RetrospectoDTO(
        Long cludeId,
        String clubeNome,
        int vitorias,
        int empates,
        int derrotas,
        int golsFeitos,
        int golsSofridos
) {}
