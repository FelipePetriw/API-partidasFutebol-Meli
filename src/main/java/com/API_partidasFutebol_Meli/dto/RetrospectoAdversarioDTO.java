package com.API_partidasFutebol_Meli.dto;

public record RetrospectoAdversarioDTO(
        String adversario,
        int vitorias,
        int empates,
        int derrotas,
        int golsFeitos,
        int golsSofridos
) {}
