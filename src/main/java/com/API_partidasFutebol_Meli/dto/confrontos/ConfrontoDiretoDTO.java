package com.API_partidasFutebol_Meli.dto.confrontos;

import java.util.List;

public record ConfrontoDiretoDTO(
        List<ConfrontoPartidaDTO> partidas,
        ConfrontoResumoDTO clube1,
        ConfrontoResumoDTO clube2
) {}
