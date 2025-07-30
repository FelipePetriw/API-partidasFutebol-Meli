package com.API_partidasFutebol_Meli.controllerTest;

import com.API_partidasFutebol_Meli.controller.EstadioController;
import com.API_partidasFutebol_Meli.dto.estadio.EstadioRequestDTO;
import com.API_partidasFutebol_Meli.dto.estadio.EstadioResponseDTO;
import com.API_partidasFutebol_Meli.service.EstadioService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

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

    @Test
    void deveEditarEstadio() {
        EstadioRequestDTO dto = new EstadioRequestDTO("Arena Renovada");
        EstadioResponseDTO response = new EstadioResponseDTO(1L, "Arena Renovada");

        when(estadioService.editar(1L, dto)).thenReturn(response);

        ResponseEntity<EstadioResponseDTO> result = controller.editar(1L, dto);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals("Arena Renovada", result.getBody().nome());
    }

    @Test
    void deveBuscarEstadioPorId() {
        EstadioResponseDTO  response = new EstadioResponseDTO(3L, "Estádio Nacional");

        when(estadioService.buscarPorId(3L)).thenReturn(response);

        ResponseEntity<EstadioResponseDTO> result = controller.buscarPorId(3L);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals("Estádio Nacional", result.getBody().nome());
    }

    @Test
    void deveListarEstadios() {
        Pageable pageable = Pageable.unpaged();
        Page<EstadioResponseDTO> page = new PageImpl<>(List.of(
                new EstadioResponseDTO(1L, "Estádio A"),
                new EstadioResponseDTO(2L, "Estádio B")
        ));

        when(estadioService.listarTodos(pageable)).thenReturn(page);

        ResponseEntity<Page<EstadioResponseDTO>> result = controller.listar(pageable);
        assertEquals(2, result.getBody().getContent().size());
        assertEquals("Estádio A", result.getBody().getContent().get(0).nome());
    }
}
