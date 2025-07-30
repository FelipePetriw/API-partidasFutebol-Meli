package com.API_partidasFutebol_Meli.dto.partida;

public record PartidaFiltroDTO(
        Long clubeId,
        Long estadioId,
        Boolean goleada
) {}
