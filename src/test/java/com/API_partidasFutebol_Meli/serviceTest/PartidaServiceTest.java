package com.API_partidasFutebol_Meli.serviceTest;

import com.API_partidasFutebol_Meli.dto.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.entity.Partida;
import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import com.API_partidasFutebol_Meli.repository.EstadioRepository;
import com.API_partidasFutebol_Meli.repository.PartidaRepository;
import com.API_partidasFutebol_Meli.service.PartidaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private EstadioRepository estadioRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private PartidaService partidaService;

    @Test
    void deveCadastrarPartidaComSucesso() {

        Clube mandante = new Clube(1L, "Time A", "SP", LocalDate.of(2000, 1, 1), true);
        Clube visitante = new Clube(2L, "Time B", "RJ", LocalDate.of(2001, 1, 1), true);
        Estadio estadio = new Estadio(1L, "Estadio X");

        PartidaRequestDTO dto = new PartidaRequestDTO(
                1L, 2L, 1L,
                LocalDateTime.now().minusDays(1),
                2,1
        );

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));
        when(partidaRepository.existsByClubeMandanteOrClubeVisitanteAndDataHoraBetween(any(), any(), any(), any())).thenReturn(false);
        when(partidaRepository.existsByEstadioAndDataHoraBetween(any(), any(), any())).thenReturn(false);
        when(partidaRepository.save(any())).thenAnswer(invocation -> {
            Partida p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        PartidaResponseDTO response = partidaService.cadastrar(dto);

        assertEquals("Time A", response.clubeMandante());
        assertEquals("Time B", response.clubeVisitante());
    }

    @Test
    void naoDeveCadastrarPartidaComClubesIguais() {
        PartidaRequestDTO dto = new PartidaRequestDTO(1L, 1L, 1L, LocalDateTime.now(), 1, 1);
        assertThrows(BadRequestException.class, () -> partidaService.cadastrar(dto));
    }
}
