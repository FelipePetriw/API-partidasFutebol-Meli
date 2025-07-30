package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoResumoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfrontoResumoDTOTest {

    @Test
    void deveCriarConfrontoResumoDTO() {
        ConfrontoResumoDTO dto = new ConfrontoResumoDTO("Time A", 1, 0, 2, 5, 3);
        assertEquals("Time A", dto.clube());
        assertEquals(1, dto.vitorias());
        assertEquals(0, dto.empates());
    }
}
