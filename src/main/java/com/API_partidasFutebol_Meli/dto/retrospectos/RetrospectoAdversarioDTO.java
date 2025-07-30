package com.API_partidasFutebol_Meli.dto.retrospectos;

public record RetrospectoAdversarioDTO(
        String adversario,
        int vitorias,
        int empates,
        int derrotas,
        int golsFeitos,
        int golsSofridos
) {}
