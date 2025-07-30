package com.API_partidasFutebol_Meli.dtoTest;

import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoDiretoDTO;
import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoPartidaDTO;
import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoResumoDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfrontoDiretoDTOTest {

    @Test
    void deveCriarConfrontoDiretoDTO() {
        List<ConfrontoPartidaDTO> partidas = new ArrayList<>();

        ConfrontoResumoDTO clube1 = new ConfrontoResumoDTO("Time A", 2, 0, 1, 5, 2); // preencha os campos reais
        ConfrontoResumoDTO clube2 = new ConfrontoResumoDTO("Time B", 1,1, 0, 2, 1); // idem

        ConfrontoDiretoDTO dto = new ConfrontoDiretoDTO(partidas, clube1, clube2);

        assertEquals("Time A", dto.clube1().clube());
        assertEquals(partidas, dto.partidas());
    }
}
