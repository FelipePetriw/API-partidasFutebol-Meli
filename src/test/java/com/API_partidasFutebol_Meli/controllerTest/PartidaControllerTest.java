package com.API_partidasFutebol_Meli.controllerTest;

import com.API_partidasFutebol_Meli.controller.PartidaController;
import com.API_partidasFutebol_Meli.dto.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.service.PartidaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PartidaControllerTest {

    @Mock
    private PartidaService partidaService;

    @InjectMocks
    private PartidaController controller;

    public PartidaControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarPartida() {
        PartidaRequestDTO dto = new PartidaRequestDTO(1L, 2L, 3L, LocalDateTime.now(), 2, 1);
        PartidaResponseDTO response = new PartidaResponseDTO(1L, "Time A", "Time B", "Estádio", dto.dataHora(), 2, 1);
        when(partidaService.cadastrar(dto)).thenReturn(response);

        ResponseEntity<PartidaResponseDTO> result = controller.cadastrar(dto);
        assertEquals(201, result.getStatusCodeValue());
        Assertions.assertNotNull(result.getBody());
        assertEquals("Time A", result.getBody().clubeMandante());
    }
}
