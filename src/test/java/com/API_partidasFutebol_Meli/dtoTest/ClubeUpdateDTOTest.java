package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.clube.ClubeUpdateDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClubeUpdateDTOTest {

    @Test
    void deveCriarUpdateDTO() {
        ClubeUpdateDTO dto = new ClubeUpdateDTO("Palmeiras", "SP", LocalDate.of(1900, 1, 1));
        assertEquals("Palmeiras", dto.nome());
        assertEquals("SP", dto.siglaEstado());
    }
}
