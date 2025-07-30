package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoPartidaDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfrontoPartidaDTOTest {

    @Test
    void deveCRiarConfrontoPartidaDTO() {
        LocalDateTime dataHora = LocalDateTime.now();
        ConfrontoPartidaDTO dto = new ConfrontoPartidaDTO(dataHora, "Arena", "Time A", "Time B", 3, 2);
        assertEquals("Time A", dto.mandante());
        assertEquals(dataHora, dto.dataHora());
    }
}
