package com.API_partidasFutebol_Meli.controllerTest;

import com.API_partidasFutebol_Meli.controller.PartidaController;
import com.API_partidasFutebol_Meli.dto.partida.PartidaFiltroDTO;
import com.API_partidasFutebol_Meli.dto.partida.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.partida.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.service.PartidaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    void deveAtualizarPartida() {
        PartidaRequestDTO dto = new PartidaRequestDTO(1L, 2L, 3L, LocalDateTime.now(), 2, 1);
        PartidaResponseDTO response = new PartidaResponseDTO(
                1L, "Time A", "Time B", "Estádio", dto.dataHora(), 2, 1);

        when(partidaService.atualizar(1L, dto)).thenReturn(response);

        ResponseEntity<PartidaResponseDTO> result = controller.atualizar(1L, dto);
        assertEquals(200, result.getStatusCodeValue());
        Assertions.assertNotNull(result.getBody());
        assertEquals("Time A", result.getBody().clubeMandante());
    }

    @Test
    void deveRemoverPartida() {
        ResponseEntity<Void>  result = controller.remover(1L);
        assertEquals(204, result.getStatusCodeValue());
    }

    @Test
    void deveBuscarPartidaPorId() {
        LocalDateTime data = LocalDateTime.of(2025, 7, 20, 16, 0);
        PartidaResponseDTO response = new PartidaResponseDTO(10L, "Mandante", "Visitante", "Arena", data, 1,1);

        when(partidaService.buscarPorId(10L)).thenReturn(response);

        ResponseEntity<PartidaResponseDTO> result = controller.buscarPorId(10L);
        assertEquals("Mandante", result.getBody().clubeMandante());
        assertEquals("Visitante", result.getBody().clubeVisitante());
    }

    @Test
    void deveListarPartidasComFiltro(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dataHora").descending());

        Page<PartidaResponseDTO> page = new PageImpl<>(List.of(
                new PartidaResponseDTO(1L, "Time A", "Time B", "Estádio X", LocalDateTime.now(), 2, 1),
                new PartidaResponseDTO(2L, "Time C", "Time D", "Estádio Y", LocalDateTime.now(), 3,2)
        ));

        PartidaFiltroDTO filtro = new PartidaFiltroDTO(1L, null, false);
        when(partidaService.listar(filtro, pageable)).thenReturn(page);

        ResponseEntity<Page<PartidaResponseDTO>> result = controller.listar(1L, null, false, pageable);
        assertEquals(2, result.getBody().getContent().size());
        assertEquals("Time A", result.getBody().getContent().get(0).clubeMandante());
    }
}
