package com.API_partidasFutebol_Meli.dto;

public record PartidaFiltroDTO(
        Long clubeId,
        Long estadioId,
        Boolean goleada
) {}
