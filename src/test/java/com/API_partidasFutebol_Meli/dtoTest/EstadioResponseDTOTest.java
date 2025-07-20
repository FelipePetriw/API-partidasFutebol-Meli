package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.EstadioResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EstadioResponseDTOTest {

    @Test
    void deveCriarEstadioResponseDTO() {
        EstadioResponseDTO dto = new EstadioResponseDTO(1L, "Beira-Rio");
        assertEquals(1L, dto.id());
        assertEquals("Beira-Rio", dto.nome());
    }
}
