package com.API_partidasFutebol_Meli.serviceTest;

import com.API_partidasFutebol_Meli.controller.EstadioController;
import com.API_partidasFutebol_Meli.dto.EstadioRequestDTO;
import com.API_partidasFutebol_Meli.dto.EstadioResponseDTO;
import com.API_partidasFutebol_Meli.dto.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import com.API_partidasFutebol_Meli.repository.EstadioRepository;
import com.API_partidasFutebol_Meli.repository.PartidaRepository;
import com.API_partidasFutebol_Meli.service.EstadioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
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

    @Test
    void deveEditarEstadioComSucesso() {
        EstadioRequestDTO dto = new EstadioRequestDTO("Arena Futuro");
        Estadio original =  new Estadio(1L, "Estádio Antigo", "SP", 25000);

        when(estadioRepository.findById(1L)).thenReturn(Optional.of(original));
        when(estadioRepository.findByNomeIgnoreCase("Arena Futuro")).thenReturn(Optional.empty());
        when(estadioRepository.save(any(Estadio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EstadioResponseDTO response = estadioService.editar(1L, dto);
        assertEquals("Arena Futuro", response.nome());
    }

    @Test
    void deveLancarExceptionAoEditarEstadioInexistente() {
        EstadioRequestDTO dto = new EstadioRequestDTO("Arena Desconhecida");
        when(estadioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> estadioService.editar(1L, dto));
    }

    @Test
    void deveBuscarEstadioPorId() {
        Estadio estadio = new Estadio(1L, "Estádio Solar",  "SP", 25000);
        when(estadioRepository.findById(1L)).thenReturn(Optional.of(estadio));

        EstadioResponseDTO response = estadioService.buscarPorId(1L);
        assertEquals("Estádio Solar", response.nome());
    }

    @Test
    void deveLancarExceptionAoBuscarEstadioPorIdInexistente() {
        when(estadioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> estadioService.buscarPorId(1L));
    }

    @Test
    void deveListarTodosEstadios() {
        Estadio e1 = new Estadio(1L, "Arena Um", "SP", 25000);
        Estadio e2 =  new Estadio(2L, "Arena Dois", "SP", 25000);
        Page<Estadio> page = new PageImpl<>(List.of(e1, e2));

        Pageable pageable = PageRequest.of(0, 2);
        when(estadioRepository.findAll(pageable)).thenReturn(page);

        Page<EstadioResponseDTO> resultado = estadioService.listarTodos(pageable);

        assertEquals(2, resultado.getContent().size());
        assertEquals("Arena Dois", resultado.getContent().get(1).nome());
    }
}
