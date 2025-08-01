package com.API_partidasFutebol_Meli.service;

import com.API_partidasFutebol_Meli.dto.partida.PartidaFiltroDTO;
import com.API_partidasFutebol_Meli.dto.partida.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.partida.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.entity.Partida;
import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.exception.ConflictException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import com.API_partidasFutebol_Meli.repository.EstadioRepository;
import com.API_partidasFutebol_Meli.repository.PartidaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final EstadioRepository estadioRepository;
    private final ClubeRepository clubeRepository;

    public PartidaService(PartidaRepository partidaRepository, EstadioRepository estadioRepository, ClubeRepository clubeRepository) {
        this.partidaRepository = partidaRepository;
        this.estadioRepository = estadioRepository;
        this.clubeRepository = clubeRepository;
    }

    @Transactional
    public PartidaResponseDTO cadastrar(PartidaRequestDTO dto) {
        if (dto.clubeMandanteId().equals(dto.clubeVisitanteId())) {
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

        if (dto.dataHora().isBefore(mandante.getDataCriacao().atStartOfDay()) || dto.dataHora().isBefore(visitante.getDataCriacao().atStartOfDay())) {
            throw new ConflictException("Data da partida anterior à criação de um dos clubes.");
        }

        var inicio = dto.dataHora().minusHours(48);
        var fim = dto.dataHora().plusHours(48);
        boolean conflitoHorario = partidaRepository.existsByClubeMandanteOrClubeVisitanteAndDataHoraBetween(mandante, visitante, inicio, fim);

        if (conflitoHorario) {
           throw new ConflictException("Um dos clubes já tem partida próxima (menos de 48h");
        }

        boolean estadioOcupado = partidaRepository.existsByEstadioAndDataHoraBetween(
                estadio, dto.dataHora().toLocalDate().atStartOfDay(), dto.dataHora().toLocalDate().atTime(23, 59));
        if (estadioOcupado) {
            throw new ConflictException("Já existe partida nesse estádio no mesmo dia.");
        }

        Partida partida = new Partida();
        partida.setClubeMandante(mandante);
        partida.setClubeVisitante(visitante);
        partida.setEstadio(estadio);
        partida.setDataHora(dto.dataHora());
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

    @Transactional
    public PartidaResponseDTO atualizar(Long id, PartidaRequestDTO dto) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada."));

        if (dto.clubeMandanteId().equals(dto.clubeVisitanteId())) {
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

        if (dto.dataHora().isBefore(mandante.getDataCriacao().atStartOfDay()) || dto.dataHora().isBefore(visitante.getDataCriacao().atStartOfDay())) {
            throw new ConflictException("Data da partida anterior à criação de um dos clubes.");
        }

        LocalDateTime inicio = dto.dataHora().minusHours(48);
        LocalDateTime fim = dto.dataHora().plusHours(48);

        boolean conflitoHorario = partidaRepository.findAll().stream().anyMatch(p ->
                (p.getClubeMandante().getId().equals(mandante.getId()) || p.getClubeVisitante().getId().equals(mandante.getId()) ||
                                p.getClubeMandante().getId().equals(visitante.getId()) || p.getClubeVisitante().getId().equals(visitante.getId())) &&
                                (p.getDataHora().isAfter(inicio) && p.getDataHora().isBefore(fim))
                );

        if (conflitoHorario) {
            throw new ConflictException("Conflito de horário com outra partida dos clubes.");
        }

        boolean estadioOcupado = partidaRepository.findAll().stream().filter(
                p -> !p.getId().equals(AbstractPersistable_.id)).anyMatch(p ->
                            p.getEstadio().getId().equals(estadio.getId()) &&
                                    p.getDataHora().toLocalDate().equals(dto.dataHora().toLocalDate())
                        );

        if (estadioOcupado) {
            throw new ConflictException("Já existe partida nesse estadio no mesmo dia.");
        }

        partida.setClubeMandante(mandante);
        partida.setClubeVisitante(visitante);
        partida.setEstadio(estadio);
        partida.setDataHora(dto.dataHora());
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

    @Transactional
    public void remover(Long id) {
            Partida partida = partidaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada."));
            partidaRepository.delete(partida);
    }

    @Transactional(readOnly = true)
    public PartidaResponseDTO buscarPorId(Long id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada."));

        return new PartidaResponseDTO(
                partida.getId(),
                partida.getClubeMandante().getNome(),
                partida.getClubeVisitante().getNome(),
                partida.getEstadio().getNome(),
                partida.getDataHora(),
                partida.getGolsMandante(),
                partida.getGolsVisitante()
        );
    }

    @Transactional(readOnly = true)
    public Page<PartidaResponseDTO> listar(PartidaFiltroDTO filtro, Pageable pageable) {
        Specification<Partida> spec = ((root, query, criteriaBuilder) -> null);

        if (filtro.clubeId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("clubeMandante").get("id"), filtro.clubeId()),
                    criteriaBuilder.equal(root.get("clubeVisitante").get("id"), filtro.clubeId())
            ));
        }

        if (filtro.estadioId() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("estadio").get("id"), filtro.estadioId()
            ));
        }

        if (Boolean.TRUE.equals(filtro.goleada())) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.ge(criteriaBuilder.diff(root.get("golsMandante"), root.get("golsVisitante")), 3),
                    criteriaBuilder.ge(criteriaBuilder.diff(root.get("golsVisitante"), root.get("golsMandante")), 3)
            ));
        }

        return partidaRepository.findAll(spec, pageable)
                .map(p -> new PartidaResponseDTO(
                        p.getId(),
                        p.getClubeMandante().getNome(),
                        p.getClubeVisitante().getNome(),
                        p.getEstadio().getNome(),
                        p.getDataHora(),
                        p.getGolsMandante(),
                        p.getGolsVisitante()
                ));
    }
}
