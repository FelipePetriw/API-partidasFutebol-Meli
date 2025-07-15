package com.API_partidasFutebol_Meli.service;

import com.API_partidasFutebol_Meli.dto.*;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Partida;
import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import com.API_partidasFutebol_Meli.repository.PartidaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ClubeService {

    private final ClubeRepository repository;
    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;

    public ClubeService(ClubeRepository repository, ClubeRepository clubeRepository, PartidaRepository partidaRepository) {
        this.repository = repository;
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
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

    @Transactional
    public void inativar(Long id) {
        Clube clube = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Clube não encontrado."));

        clube.setAtivo(false);
        repository.save(clube);
    }

    @Transactional(readOnly = true)
    public ClubeResponseDTO buscarPorId(Long id) {
        Clube clube = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Clube não encontrado."));

        return new ClubeResponseDTO(
                clube.getId(),
                clube.getNome(),
                clube.getSiglaEstado(),
                clube.getDataCriacao(),
                clube.getAtivo()
        );
    }

    @Transactional
    public Page<ClubeResponseDTO> listar(ClubeFiltroDTO filtro, Pageable pageable) {
        Specification<Clube> spec = ((root, query, criteriaBuilder) -> null);

        if(filtro.nome() != null && !filtro.nome().isBlank()) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + filtro.nome().toLowerCase() + "%"));
        }

        if(filtro.siglaEstado() != null && !filtro.siglaEstado().isBlank()) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("siglaEstado"), filtro.siglaEstado()));
        }

        if (filtro.ativo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("ativo"), filtro.ativo()));
        }

        return repository.findAll(spec, pageable).map(clube -> new ClubeResponseDTO(
                clube.getId(),
                clube.getNome(),
                clube.getSiglaEstado(),
                clube.getDataCriacao(),
                clube.getAtivo()

        ));
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
