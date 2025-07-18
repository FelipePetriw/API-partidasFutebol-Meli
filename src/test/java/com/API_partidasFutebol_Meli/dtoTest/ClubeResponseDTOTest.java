package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.ClubeResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClubeResponseDTOTest {

    @Test
    void deveRetornarDadosdoResponse() {
        ClubeResponseDTO dto = new ClubeResponseDTO(1L, "Santos", "SP", LocalDate.of(1912, 4, 14), true);
        assertEquals(1L, dto.id());
        assertEquals("Santos", dto.nome());
        assertEquals("SP", dto.siglaEstado());
        assertTrue(dto.ativo());
    }
}
