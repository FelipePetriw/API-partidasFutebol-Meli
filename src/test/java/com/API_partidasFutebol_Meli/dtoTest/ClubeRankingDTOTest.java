package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.ClubeRankingDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClubeRankingDTOTest {

    @Test
    void deveCriarClubeRankingDTO() {
        ClubeRankingDTO dto = new ClubeRankingDTO("Time A", 10, 5, 3, 2);
        assertEquals("Time A", dto.clube());
        assertEquals(2, dto.jogos());
        assertEquals(10, dto.pontos());
    }
}
