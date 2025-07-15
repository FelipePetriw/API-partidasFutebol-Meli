package com.API_partidasFutebol_Meli.service;

import com.API_partidasFutebol_Meli.dto.EstadioRequestDTO;
import com.API_partidasFutebol_Meli.dto.EstadioResponseDTO;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import com.API_partidasFutebol_Meli.repository.EstadioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadioService {

    private final EstadioRepository estadioRepository;

    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    @Transactional
    public EstadioResponseDTO cadastrar(EstadioRequestDTO dto) {
        estadioRepository.findByNomeIgnoreCase(dto.nome())
                .ifPresent(estadio -> {
                    throw new RecursoDuplicadoException("Estadio já cadastrado.");
                });

        Estadio estadio = new Estadio();
        estadio.setNome(dto.nome());

        Estadio salvo = estadioRepository.save(estadio);
        return new EstadioResponseDTO(salvo.getId(), salvo.getNome());
    }
}
