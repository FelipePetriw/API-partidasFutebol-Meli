package com.API_partidasFutebol_Meli.controllerTest;

import com.API_partidasFutebol_Meli.controller.ClubeController;
import com.API_partidasFutebol_Meli.dto.ClubeResponseDTO;
import com.API_partidasFutebol_Meli.dto.ClubeUpdateDTO;
import com.API_partidasFutebol_Meli.service.ClubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ClubeControllerTest {

    @InjectMocks
    private ClubeController controller;

    @Mock
    private ClubeService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveEditarClube() {
        ClubeUpdateDTO dto = new ClubeUpdateDTO("Novo", "SP", LocalDate.of(1990, 1, 1));
        ClubeResponseDTO response = new ClubeResponseDTO(1L, "Novo", "SP", LocalDate.of(1900, 1, 1), true);
        when(service.editar(1L, dto)).thenReturn(response);
        ResponseEntity<ClubeResponseDTO> result = controller.editar(1L, dto);
        assertEquals("Novo", result.getBody().nome());
    }

    @Test
    void deveInativarClube() {
        ResponseEntity<Void> result = controller.inativar(2L);
        assertEquals(204, result.getStatusCode().value());
    }

    @Test
    void deveBuscarPorId() {
        ClubeResponseDTO response = new ClubeResponseDTO(1L, "Novo", "SP", LocalDate.of(1990, 1, 1), true);
        when(service.buscarPorId(1L)).thenReturn(response);
        ResponseEntity<ClubeResponseDTO> result = controller.buscarPorId(1L);
        assertEquals("Novo", result.getBody().nome());
    }
}
