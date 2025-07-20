package com.API_partidasFutebol_Meli.serviceTest;

import com.API_partidasFutebol_Meli.dto.PartidaFiltroDTO;
import com.API_partidasFutebol_Meli.dto.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.entity.Partida;
import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.exception.ConflictException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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

    Clube mandante = new Clube(1L, "Time A", "SP", LocalDate.of(2000, 1, 1), true);
    Clube visitante = new Clube(2L, "Time B", "RJ", LocalDate.of(2001, 1, 1), true);
    Estadio estadio = new Estadio(1L, "Estadio X");

    @Test
    void deveCadastrarPartidaComSucesso() {

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

    @Test
    void deveLancarBadRequestQuandoClubesForemIguais() {
        PartidaRequestDTO dto = new PartidaRequestDTO(1L, 1L, 1L, LocalDateTime.now(), 0, 0);
        assertThrows(BadRequestException.class, () -> partidaService.cadastrar(dto));
    }

    @Test
    void deveLancarExceptionSeClubeMandanteNaoEncontrado() {
        PartidaRequestDTO dto = new PartidaRequestDTO(99L, 2L, 1L, LocalDateTime.now(), 0, 0);
        when(clubeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> partidaService.cadastrar(dto));
    }

    @Test
    void deveRemoverPartida() {
        Partida partida = new Partida();
        partida.setId(123L);
        when(partidaRepository.findById(123L)).thenReturn(Optional.of(partida));

        partidaService.remover(123L);

        verify(partidaRepository, times(1)).delete(partida);
    }

    @Test
    void deveBuscarPartidaPorId() {
        Partida partida = new Partida();
        partida.setId(99L);
        partida.setClubeMandante(mandante);
        partida.setClubeVisitante(visitante);
        partida.setEstadio(estadio);
        partida.setDataHora(LocalDateTime.now());
        partida.setGolsMandante(3);
        partida.setGolsVisitante(2);

        when(partidaRepository.findById(99L)).thenReturn(Optional.of(partida));

        PartidaResponseDTO response = partidaService.buscarPorId(99L);

        assertEquals(3, response.golsMandante());
        assertEquals("Time B", response.clubeVisitante());
    }

    @Test
    void deveLancarConflictSeClubeInativo() {
        mandante.setAtivo(false);
        PartidaRequestDTO dto = new PartidaRequestDTO(1L, 2L, 1L, LocalDateTime.now(), 0, 0);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));

        assertThrows(ConflictException.class, () -> partidaService.cadastrar(dto));
    }

    @Test
    void deveAtualizarPartidaComSucesso() {
        Partida partidaExistente = new Partida();
        partidaExistente.setId(20L);

        PartidaRequestDTO dto = new PartidaRequestDTO(1L, 2L, 3L, LocalDateTime.now().plusDays(1), 3, 2);

        when(partidaRepository.findById(20L)).thenReturn(Optional.of(partidaExistente));
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(mandante));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(visitante));
        when(estadioRepository.findById(3L)).thenReturn(Optional.of(estadio));
        when(partidaRepository.findAll()).thenReturn(List.of());
        when(partidaRepository.save(any())).thenReturn(partidaExistente);

        PartidaResponseDTO response = partidaService.atualizar(20L, dto);
        assertEquals("Time A",  response.clubeMandante());
    }

    @Test
    void deveListarPartidasComFiltro() {
        PartidaFiltroDTO filtro = new PartidaFiltroDTO(1L, null, false);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Partida> pagina = new PageImpl<>(List.of());

        when(partidaRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pagina);

        Page<PartidaResponseDTO> result = partidaService.listar(filtro, pageable);
        assertTrue(result.getContent().isEmpty());
    }
}
