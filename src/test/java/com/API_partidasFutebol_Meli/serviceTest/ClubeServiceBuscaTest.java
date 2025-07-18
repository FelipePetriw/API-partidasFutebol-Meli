package com.API_partidasFutebol_Meli.serviceTest;

import com.API_partidasFutebol_Meli.dto.RetrospectoAdversarioDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Partida;
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
}
