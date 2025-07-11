package com.API_partidasFutebol_Meli.service;

import com.API_partidasFutebol_Meli.dto.ClubeRequestDTO;
import com.API_partidasFutebol_Meli.dto.ClubeResponseDTO;
import com.API_partidasFutebol_Meli.dto.ClubeUpdateDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ClubeService {

    private final ClubeRepository repository;

    public ClubeService(ClubeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClubeResponseDTO criar(ClubeRequestDTO dto) {
        repository.findByNomeAndSiglaEstado(dto.nome(),  dto.siglaEstado()).ifPresent(c -> {
            throw new RecursoDuplicadoException("Já existe um clube com este nome neste estado.");
        });

        Clube clube = new Clube();
        clube.setNome(dto.nome());
        clube.setSiglaEstado(dto.siglaEstado());
        clube.setDataCriacao(dto.dataCriacao());
        clube.setAtivo(true);

        Clube salvo = repository.save(clube);
        return new ClubeResponseDTO(salvo.getId(), salvo.getNome(), salvo.getSiglaEstado(), salvo.getDataCriacao(), salvo.getAtivo());
    }

    @Transactional
    public ClubeResponseDTO atualizar(Long id, ClubeUpdateDTO dto) throws BadRequestException {
        Clube clube = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Clube não encontrado."));

        if (clube.getNome().equals(dto.nome()) || !clube.getSiglaEstado().equals(dto.siglaEstado()))
            repository.findByNomeAndSiglaEstado(dto.nome(), dto.siglaEstado()).ifPresent(c -> {
                throw new RecursoDuplicadoException("Já existe um clube com este nome neste estado.");
            });

        if (dto.dataCriacao().isAfter(LocalDate.now())) {
            throw new BadRequestException("Data de criação não pode ser futura.");
        }

        clube.setNome(dto.nome());
        clube.setSiglaEstado(dto.siglaEstado());
        clube.setDataCriacao(dto.dataCriacao());

        return new ClubeResponseDTO(clube.getId(), clube.getNome(), clube.getSiglaEstado(),  clube.getDataCriacao(), clube.getAtivo());
    }
}
