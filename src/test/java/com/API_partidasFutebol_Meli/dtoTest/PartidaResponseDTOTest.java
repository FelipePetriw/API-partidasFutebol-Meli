package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.partida.PartidaResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartidaResponseDTOTest {

    @Test
    void deveCriarPartidaREsponseDTO () {
        LocalDateTime agora = LocalDateTime.now();
        PartidaResponseDTO dto = new PartidaResponseDTO(1L, "Inter", "Grêmio", "Beira-Rio", agora, 2, 1);
        assertEquals("Inter", dto.clubeMandante());
        assertEquals("Grêmio", dto.clubeVisitante());
    }
}
