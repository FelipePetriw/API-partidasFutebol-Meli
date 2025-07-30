package com.API_partidasFutebol_Meli.controllerTest;

import com.API_partidasFutebol_Meli.controller.ClubeController;
import com.API_partidasFutebol_Meli.dto.clube.*;
import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoDiretoDTO;
import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoResumoDTO;
import com.API_partidasFutebol_Meli.dto.retrospectos.RetrospectoAdversarioDTO;
import com.API_partidasFutebol_Meli.dto.retrospectos.RetrospectoDTO;
import com.API_partidasFutebol_Meli.service.ClubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

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
    void deveCriarClube(){
        ClubeRequestDTO dto = new ClubeRequestDTO("Time X", "SC", LocalDate.of(1900, 1, 1));
        ClubeResponseDTO response = new ClubeResponseDTO(1L, "Time X", "SC", LocalDate.of(1900, 1, 1), true);
        when(service.criar(dto)).thenReturn(response);

        ResponseEntity<ClubeResponseDTO> result = controller.criar(dto);
        assertEquals(201, result.getStatusCodeValue());
        assertEquals("Time X", result.getBody().nome());
    }

    @Test
    void deveEditarClube() {
        ClubeUpdateDTO dto = new ClubeUpdateDTO("Novo", "SP", LocalDate.of(1990, 1, 1));
        ClubeResponseDTO response = new ClubeResponseDTO(1L, "Novo", "SP", LocalDate.of(1900, 1, 1), true);
        when(service.editar(1L, dto)).thenReturn(response);
        ResponseEntity<ClubeResponseDTO> result = controller.editar(1L, dto);
        assertEquals("Novo", result.getBody().nome());
        assertEquals("SP", result.getBody().siglaEstado());
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

    @Test
    void deveRetornarConfrontoDireto() {
        ConfrontoDiretoDTO response = new ConfrontoDiretoDTO(
                List.of(), //Lista de Partidas
                new ConfrontoResumoDTO("Clube A", 2, 1, 1, 5, 3),
                new ConfrontoResumoDTO("Clube B", 1, 1, 2, 3, 5)
        );

        when(service.confrontoPorDireto(1L, 2L)).thenReturn(response);

        ResponseEntity<ConfrontoDiretoDTO> result = controller.confronto(1L, 2L);
        assertEquals("Clube A", result.getBody().clube1().nome());
        assertEquals("Clube B", result.getBody().clube2().nome());
    }

    @Test
    void deveListarClubescomFiltro() {
        ClubeFiltroDTO filtro = new ClubeFiltroDTO("Time", "SP", true);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());
        Page<ClubeResponseDTO> page = new PageImpl<>(List.of(
                new ClubeResponseDTO(5L, "Time A", "SP", LocalDate.of(1980, 5, 1), true),
                new ClubeResponseDTO(6L, "Time B", "SP", LocalDate.of(1900, 1, 1), true)
        ));

        when(service.listar(filtro, pageable)).thenReturn(page);

        ResponseEntity<Page<ClubeResponseDTO>> result = controller.listar("Time", "SP", true, pageable);
        assertEquals(2, result.getBody().getContent().size());
    }

    @Test
    void deveRetornarRetrospectoGeral() {
        RetrospectoDTO response = new RetrospectoDTO(10L,"Time", 5, 3, 2, 12, 8);
        when(service.retrospectoGeral(7L)).thenReturn(response);

        ResponseEntity<RetrospectoDTO> result = controller.retrospectoGeral(7L);
        assertEquals(5L, result.getBody().vitorias());
    }

    @Test
    void deveRetornarRetrospectoPorAdversario() {
        List<RetrospectoAdversarioDTO> resposta = List.of(
                new RetrospectoAdversarioDTO("Adversario X", 2, 1, 0, 3, 5),
                new RetrospectoAdversarioDTO("Adversario Y", 1, 2, 1, 5, 3)
        );

        when(service.retrospectoPorAdversario(7L)).thenReturn(resposta);

        ResponseEntity<List<RetrospectoAdversarioDTO>>  result = controller.retrospectoPorAdversario(7L);
        assertEquals(2, result.getBody().size());
    }

    @Test
    void deveRetornarRankingPorCriterio() {
        List<ClubeRankingDTO> ranking = List.of(
                new ClubeRankingDTO("Time A", 30, 25, 10, 15),
                new ClubeRankingDTO("Time B", 30, 25, 10, 15)
        );

        when(service.ranking("pontos")).thenReturn(ranking);

        ResponseEntity<List<ClubeRankingDTO>> result = controller.ranking("pontos");
        assertEquals(2, result.getBody().size());
        assertEquals("Time A", result.getBody().get(0).nome());
    }
}
