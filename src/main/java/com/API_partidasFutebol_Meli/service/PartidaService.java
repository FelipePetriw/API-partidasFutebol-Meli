package com.API_partidasFutebol_Meli.service;

import com.API_partidasFutebol_Meli.dto.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.entity.Partida;
import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import com.API_partidasFutebol_Meli.repository.PartidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;

    public PartidaService(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    @Transactional
    public PartidaResponseDTO cadastrar(PartidaRequestDTO dto) {
        if (dto.clubeMandante().equals(dto.clubeVisitante())) {
            throw new BadRequestException("Clube mandante e visitante não podem ser iguais.");
        }

        Clube mandante = clubeRepository.findById(dto.clubeMandanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Clube mandante não encontrado."));
        Clube visitante = clubeRepository.findById(dto.clubeVisitanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Clube visitante não encontrado."));
        Estadio estadio = estadioRepository.findById(dto.estadioId())
                .orElseThrow(() -> new ResourceNotFoundException("Estádio não encontrado."));

        if(!mandante.getAtivo() || !visitante.getAtivo()){
            throw new ConflictException("Partida não pode ser cadastrada com clube inativo.");
        }

        if (dto.dataHora().isBefore(mandante.getDataCriacao()) || dto.dataHora().isBefore(visitante.getDataCriacao())) {
            throw new ConflictException("Data da partida anterior à criação de um dos clubes.");
        }

        var inicio = dto.dataHora().minusHours(48);
        var fim = dto.dataHora().plusHours(48);
        boolean conflitoHorario = partidaRepository.existsByClubeMandanteOrClubeVisitanteAndDataHoraBetween(mandante, visitante, inicio, fim);

        if (conflitoHorario) {
            throw new ConflictException("Um dos clubes já tem partida próxima (menos de 48h");
        }

        boolean estadioOcupado = partidaRepository.existsByEstadioAndDataHoraBetween(
                estadio, dto.dataHora().toLocalDate().atStartOfDay(), dto.dataHora().atTime(23, 59));
        if (estadioOcupado) {
            throw new ConflictException("Já existe partida nesse estádio no mesmo dia.");
        }

        Partida partida = new Partida();
        partida.setClubeMandante(mandante);
        partida.setClubeVisitante(visitante);
        partida.setEstadio(estadio);
        partida.setDataHora(dto.dataHora().atStartOfDay());
        partida.setGolsMandante(dto.golsMandante());
        partida.setGolsVisitante(dto.golsVisitante());

        var salvo = partidaRepository.save(partida);
        return new PartidaResponseDTO(
                salvo.getId(),
                mandante.getNome(),
                visitante.getNome(),
                estadio.getNome(),
                salvo.getDataHora(),
                salvo.getGolsMandante(),
                salvo.getGolsVisitante()
        );
    }
}
