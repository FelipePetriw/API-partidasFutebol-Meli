package com.API_partidasFutebol_Meli;

import com.API_partidasFutebol_Meli.dto.ClubeRequestDTO;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
}
