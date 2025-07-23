package com.API_partidasFutebol_Meli.dto;

public record ClubeRankingDTO(
        String clube,
        int pontos,
        int gols,
        int vitorias,
        int jogos
) {
    public String nome() {
        return clube;
    }
}
