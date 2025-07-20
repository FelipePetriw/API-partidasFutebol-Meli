package com.API_partidasFutebol_Meli.serviceTest;

import com.API_partidasFutebol_Meli.dto.ClubeFiltroDTO;
import com.API_partidasFutebol_Meli.dto.ClubeRequestDTO;
import com.API_partidasFutebol_Meli.dto.ClubeResponseDTO;
import com.API_partidasFutebol_Meli.dto.ClubeUpdateDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import com.API_partidasFutebol_Meli.service.ClubeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClubeServiceTest {

    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private ClubeService clubeService;

    @Test
    void deveCadastarClubeComSucesso() {
        var dto = new ClubeRequestDTO("Palmeiras", "SP", LocalDate.of(1914, 8, 26));

        when(clubeRepository.findByNomeAndSiglaEstado("Palmeiras", "SP")).thenReturn(Optional.empty());
        when(clubeRepository.save(any())).thenAnswer(invocation -> {
            Clube clube = invocation.getArgument(0);
            clube.setId(1L);
            return clube;
        });

        var result = clubeService.criar(dto);

        assertEquals("Palmeiras", result.nome());
        assertEquals("SP", result.siglaEstado());
        assertEquals(1L, result.id());
    }

    @Test
    void naoDeveCadastrarClubeDuplicado() {
        var dto = new ClubeRequestDTO("Flamengo", "RJ",  LocalDate.of(1914, 8, 17));
        when(clubeRepository.findByNomeAndSiglaEstado("Flamengo", "RJ")).thenReturn(Optional.of(new Clube()));

        assertThrows(RecursoDuplicadoException.class, () -> clubeService.criar(dto));
    }

    @Test
    void naoDeveCadastrarComDataFutura() {
        var dto = new ClubeRequestDTO("Cruzeiro", "MG", LocalDate.now().plusDays(1));

        var exception = assertThrows(BadRequestException.class, () -> clubeService.criar(dto));
        assertTrue(exception.getMessage().toLowerCase().contains("data de criação não pode ser futura"));
    }

    @Test
    void deveLancarExcecaoAoCriarComDataFutura() {
        ClubeRequestDTO dto = new ClubeRequestDTO("Time Futuro", "RJ", LocalDate.now().plusDays(1));
        assertThrows(BadRequestException.class, () -> clubeService.criar(dto));
    }

    @Test
    void deveAtualizarClubeComSucesso() {
        Clube clube = new Clube(1L, "Time Antigo", "SP", LocalDate.of(1980, 1, 1), true);
        ClubeUpdateDTO dto = new ClubeUpdateDTO("Time Novo", "SP", LocalDate.of(1990, 1, 1));

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        ClubeResponseDTO result = clubeService.atualizar(1L, dto);
        assertEquals("Time Novo", result.nome());
    }

    @Test
    void deveInativarClubeComSucesso() {
        Clube clube =new Clube(1L, "Time Antigo", "SP", LocalDate.of(1980, 1, 1), true);
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        clubeService.inativar(1L);
        verify(clubeRepository).save(clube);
        assertFalse(clube.getAtivo());
    }

    @Test
    void deveBuscarClubePorIdComSucesso() {
        Clube clube = new Clube(1L, "Time Antigo", "SP", LocalDate.of(1980, 1, 1), true);
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        ClubeResponseDTO result = clubeService.buscarPorId(1L);
        assertEquals("Time Antigo", result.nome());
    }

    @Test
    void deveListarClubesComFiltroAtivo() {
        ClubeFiltroDTO filtroDTO = new ClubeFiltroDTO(null, null, true);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Clube> pageMock = new PageImpl<>(List.of(new Clube(1L, "Time Antigo", "SP", LocalDate.of(1980, 1, 1), true)));

        when(clubeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pageMock);
        Page<ClubeResponseDTO> result = clubeService.listar(filtroDTO, pageable);

        assertEquals(1, result.getContent().size());
    }
}
