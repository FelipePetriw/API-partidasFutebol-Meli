package com.API_partidasFutebol_Meli.dto.confrontos;

public record ConfrontoResumoDTO(
        String clube,
        int vitorias,
        int empates,
        int derrotas,
        int golsFeitos,
        int golsSofridos
) {
    public String nome() {
        return clube;
    }
}
