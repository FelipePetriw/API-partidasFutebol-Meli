package com.API_partidasFutebol_Meli.controllerTest;

import com.API_partidasFutebol_Meli.controller.EstadioController;
import com.API_partidasFutebol_Meli.dto.EstadioRequestDTO;
import com.API_partidasFutebol_Meli.dto.EstadioResponseDTO;
import com.API_partidasFutebol_Meli.service.EstadioService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class EstadioControllerTest {

    @Mock
    private EstadioService estadioService;

    @InjectMocks
    private EstadioController controller;

    public EstadioControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarEstadio() {
        EstadioRequestDTO dto = new EstadioRequestDTO("Arena do Grêmio");
        EstadioResponseDTO response = new EstadioResponseDTO(1L, "Arena do Grêmio");
        when(estadioService.cadastrar(dto)).thenReturn(response);

        ResponseEntity<EstadioResponseDTO> result = controller.cadastrar(dto);
        assertEquals(201, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals("Arena do Grêmio", result.getBody().nome());
    }
}
