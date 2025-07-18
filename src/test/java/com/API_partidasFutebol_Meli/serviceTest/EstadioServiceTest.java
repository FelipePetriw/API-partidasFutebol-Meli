package com.API_partidasFutebol_Meli.serviceTest;

import com.API_partidasFutebol_Meli.dto.EstadioRequestDTO;
import com.API_partidasFutebol_Meli.dto.EstadioResponseDTO;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import com.API_partidasFutebol_Meli.repository.EstadioRepository;
import com.API_partidasFutebol_Meli.service.EstadioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EstadioServiceTest {
    @Mock
    private EstadioRepository estadioRepository;
    @InjectMocks
    private EstadioService estadioService;

    @Test
    void deveCadastrarEstadioComSucesso() {
        EstadioRequestDTO dto = new EstadioRequestDTO("Maracanã");

        when(estadioRepository.findByNomeIgnoreCase("Maracanã")).thenReturn(Optional.empty());
        when(estadioRepository.save(any())).thenAnswer(invocation -> {
            Estadio estadio = invocation.getArgument(0);
            estadio.setId(1L);
            return estadio;
        });

        EstadioResponseDTO response = estadioService.cadastrar(dto);

        assertEquals("Maracanã", response.nome());
        assertEquals(1L, response.id());
    }

    @Test
    void naoDeveCadastrarEstadioDuplicado() {
        when(estadioRepository.findByNomeIgnoreCase("Arena")).thenReturn(Optional.of(new Estadio()));
        assertThrows(RecursoDuplicadoException.class, () -> estadioService.cadastrar(new EstadioRequestDTO("Arena")));
    }

    @Test
    void deveLancarErroAoEditarEstadioInexistente() {
        when(estadioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> estadioService.editar(99L, new EstadioRequestDTO("Novo Nome")));
    }
}
