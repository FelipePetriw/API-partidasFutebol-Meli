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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional(readOnly = true)
    public RetrospectoDTO retrospectoGeral(Long clubeId) {
        Clube clube = clubeRepository.findById(clubeId).orElseThrow(
                () -> new ResourceNotFoundException("Clube não encontrado.")
        );

        List<Partida> partidas = partidaRepository.findAll().stream().filter(
                partida -> partida.getClubeMandante().getId().equals(clubeId)
                        || partida.getClubeVisitante().getId().equals(clubeId)).toList();

        int vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0;

        for (Partida partida : partidas) {
            boolean mandante = partida.getClubeMandante().getId().equals(clubeId);
            int golsPro = mandante ? partida.getGolsMandante() : partida.getGolsVisitante();
            int golsContra = mandante ? partida.getGolsVisitante() : partida.getGolsMandante();

            golsFeitos += golsPro;
            golsSofridos += golsContra;

            if (golsPro > golsContra) vitorias++;
            else if (golsPro < golsContra) derrotas++;
            else empates++;
        }

        return new RetrospectoDTO(clubeId, clube.getNome(), vitorias, empates, derrotas, golsFeitos, golsSofridos);
    }

    @Transactional(readOnly = true)
    public List<RetrospectoAdversarioDTO> retrospctoPorAdversario(Long clubeId) {
        Clube clube = clubeRepository.findById(clubeId).orElseThrow(
                () -> new ResourceNotFoundException("Clube não encontrado.")
        );

        List<Partida> partidas = partidaRepository.findAll().stream().filter(
                partida -> partida.getClubeMandante().getId().equals(clubeId)
                        || partida.getClubeVisitante().getId().equals(clubeId)).toList();

        Map<String, int[]> estatisticas = new HashMap<>();

        for (Partida partida : partidas) {
            boolean mandante = partida.getClubeMandante().getId().equals(clubeId);
            String adversario = mandante ? partida.getClubeVisitante().getNome() : partida.getClubeMandante().getNome();
            int golsPro = mandante ? partida.getGolsMandante() : partida.getGolsVisitante();
            int golsContra = mandante ? partida.getGolsVisitante() : partida.getGolsMandante();

            estatisticas.putIfAbsent(adversario, new int[5]);

            int[] dados = estatisticas.get(adversario);
            dados[0] += golsPro > golsContra ? 1 : 0; //Para considerar vitórias
            dados[1] += golsPro == golsContra ? 1 : 0; //Para considerar empates
            dados[2] += golsPro < golsContra ? 1 : 0; //Para considerar derrotas
            dados[3] += golsPro; //Marca os gols feitos
            dados[4] += golsContra; //Marca os gols sofridos
        }

        return estatisticas.entrySet().stream().map(
                e -> new RetrospectoAdversarioDTO(
                        e.getKey(),
                        e.getValue()[0],
                        e.getValue()[1],
                        e.getValue()[2],
                        e.getValue()[3],
                        e.getValue()[4]
                )
        ).sorted(Comparator.comparing(RetrospectoAdversarioDTO::adversario)).toList();
    }
}
