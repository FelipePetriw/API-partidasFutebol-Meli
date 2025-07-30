package com.API_partidasFutebol_Meli.serviceTest;

import com.API_partidasFutebol_Meli.dto.clube.ClubeRankingDTO;
import com.API_partidasFutebol_Meli.dto.clube.ClubeUpdateDTO;
import com.API_partidasFutebol_Meli.dto.confrontos.ConfrontoDiretoDTO;
import com.API_partidasFutebol_Meli.dto.retrospectos.RetrospectoAdversarioDTO;
import com.API_partidasFutebol_Meli.dto.retrospectos.RetrospectoDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.entity.Partida;
import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import com.API_partidasFutebol_Meli.repository.PartidaRepository;
import com.API_partidasFutebol_Meli.service.ClubeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceBuscaTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private ClubeService clubeService;

    @Test
    void deveRetornarRetrospectoGeral() {
        Clube clube = new Clube(1L, "Time A", "SP", LocalDate.of(1980, 1,1), true);
        Partida p1 = new Partida(); //Para a Vitória
        p1.setClubeMandante(clube);
        p1.setClubeVisitante(new Clube(3L, "Time C", "MG", LocalDate.of(1990, 1, 1), true));
        p1.setGolsMandante(2);
        p1.setGolsVisitante(1);

        Partida p2 = new Partida(); //Para o empate
        p2.setClubeMandante(clube);
        p2.setClubeVisitante(new Clube(3L, "Time C", "MG", LocalDate.of(1990, 1, 1), true));
        p2.setGolsMandante(1);
        p2.setGolsVisitante(1);

        Partida p3 = new Partida(); //Para a derrota
        p3.setClubeVisitante(clube);
        p3.setClubeMandante(new Clube(2L, "Time B", "RJ", LocalDate.of(1985, 1, 1), true));
        p3.setGolsMandante(3);
        p3.setGolsVisitante(0);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        RetrospectoDTO result = clubeService.retrospectoGeral(1L);
        assertEquals(1, result.vitorias());
        assertEquals(1, result.empates());
        assertEquals(1, result.derrotas());
    }

    @Test
    void deveRetornarRetrospectoPorAdversario() {
        Clube clube1 = new Clube(1L, "Time A", "SP", LocalDate.of(2000, 1, 1), true);
        Clube clube2 = new Clube(2L, "Time B", "SP", LocalDate.of(2000, 1, 1), true);

        Partida p1 = new Partida(1L, clube1, clube2, null, LocalDateTime.now().minusDays(5), 3, 1);
        Partida p2 = new Partida(2L, clube2, clube1, null, LocalDateTime.now().minusDays(3), 2, 2);
        Partida p3 = new Partida(3L, clube1, clube2, null, LocalDateTime.now().minusDays(1), 0, 1);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube1));
        when(partidaRepository.findAll()).thenReturn(List.of(p1, p2, p3));

        List<RetrospectoAdversarioDTO> result = clubeService.retrospectoPorAdversario(1L);

        assertEquals(1, result.size());
        RetrospectoAdversarioDTO adversario = result.get(0);
        assertEquals("Time B", adversario.adversario());
        assertEquals(1, adversario.vitorias());
        assertEquals(1, adversario.empates());
        assertEquals(1, adversario.derrotas());
        assertEquals(5, adversario.golsFeitos());
        assertEquals(4, adversario.golsSofridos());
    }

    @Test
    void deveRetornarListaVaziaSemPartidas() {
        Clube clube1 = new Clube(1L, "Time A", "SP", LocalDate.of(2000, 1, 1), true);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube1));
        when(partidaRepository.findAll()).thenReturn(List.of());

        List<RetrospectoAdversarioDTO>  result = clubeService.retrospectoPorAdversario(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deveLancarErroSeClubeNaoExiste() {
        when(clubeRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clubeService.retrospectoPorAdversario(999L));
    }

    @Test
    void deveRetornarConfrontoDiretoEntreClubes() {
        Clube clube1 = new Clube(1L, "Time A", "SP", LocalDate.of(2000, 1, 1), true);
        Clube clube2 = new Clube(2L, "Time B", "SP", LocalDate.of(2000, 1, 1), true);

        Estadio estadio = new Estadio(1L, "Estádio Original", "SP", 25000);

        Partida partida = new Partida();
        partida.setClubeMandante(clube1);
        partida.setClubeVisitante(clube2);
        partida.setEstadio(estadio);
        partida.setGolsMandante(2);
        partida.setGolsVisitante(1);
        partida.setDataHora(LocalDate.of(2024, 5, 1).atStartOfDay());

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube1));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(clube2));
        when(partidaRepository.findAll()).thenReturn(List.of(partida));

        ConfrontoDiretoDTO result = clubeService.confrontoPorDireto(1L, 2L);
        assertEquals("Time A", result.clube1().nome());
        assertEquals(1, result.clube1().vitorias());
    }

    @Test
    void deveRetornarRankingPorPontos() {
        Clube clube = new Clube(1L, "Time A", "SP", LocalDate.of(2000, 1, 1), true);
        Partida partida = new Partida();
        partida.setClubeMandante(clube);
        partida.setClubeVisitante(new Clube(2L, "Time B", "SP", LocalDate.of(2000, 1, 1), true));
        partida.setGolsMandante(3);
        partida.setGolsVisitante(1);

        when(clubeRepository.findAll()).thenReturn(List.of(clube));
        when(partidaRepository.findAll()).thenReturn(List.of(partida));

        List<ClubeRankingDTO> result = clubeService.ranking("pontos");
        assertEquals(1, result.size());
        assertEquals("Time A", result.get(0).nome());
        assertEquals(3, result.get(0).pontos());
    }

    @Test
    void deveLancarExcecaoParaCriterioInvaliddoNoRanking() {
        when(clubeRepository.findAll()).thenReturn(List.of());
        when(partidaRepository.findAll()).thenReturn(List.of());

        assertThrows(BadRequestException.class, () -> clubeService.ranking("invalid"));
    }

    @Test
    void deveRetornarNullAoEditar() {
        ClubeUpdateDTO dto = new ClubeUpdateDTO("Novo Nome", "Novo País", LocalDate.of(2000, 1, 1));
        Object result = clubeService.editar(1L, dto);
        assertNull(result);
    }
}
