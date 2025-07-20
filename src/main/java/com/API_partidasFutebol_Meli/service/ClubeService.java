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
import java.util.*;

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

        if (dto.dataCriacao().isAfter(LocalDate.now())) {
            throw new BadRequestException("data de criação não pode ser futura.");
        }

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

        if (dto.dataCriacao().isAfter(LocalDate.now())) {
            throw new BadRequestException("Data de criação não pode ser futura.");
        }

        if (clube.getNome().equals(dto.nome()) || !clube.getSiglaEstado().equals(dto.siglaEstado()))
            repository.findByNomeAndSiglaEstado(dto.nome(), dto.siglaEstado()).ifPresent(c -> {
                throw new RecursoDuplicadoException("Já existe um clube com este nome neste estado.");
            });

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
    public List<RetrospectoAdversarioDTO> retrospectoPorAdversario(Long clubeId) {
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

    @Transactional(readOnly = true)
    public ConfrontoDiretoDTO confrontoPorDireto(Long clube1Id, Long clube2Id) {
        Clube clube1 = clubeRepository.findById(clube1Id).orElseThrow(
                () -> new ResourceNotFoundException("Clube 1 não encontrado."));
        Clube clube2 = clubeRepository.findById(clube2Id).orElseThrow(
                () -> new ResourceNotFoundException("Clube 2 não encontrado."));

        List<Partida> partidas = partidaRepository.findAll().stream().filter(
                partida -> {
                    Long mandante = partida.getClubeMandante().getId();
                    Long visitante = partida.getClubeVisitante().getId();
                    return (mandante.equals(clube1Id) && visitante.equals(clube2Id))
                            || (mandante.equals(clube2Id) && visitante.equals(clube1Id));
                }).toList();

        List<ConfrontoPartidaDTO> listaPartidas = new ArrayList<>();
        int[] stats1 = new int[5];
        int[] stats2 = new int[5];

        for (Partida partida : partidas) {
            boolean clube1Mandante = partida.getClubeMandante().getId().equals(clube1Id);
            boolean clube1Jogou = clube1Mandante || partida.getClubeVisitante().getId().equals(clube1Id);

            int golsClube1 = clube1Mandante ? partida.getGolsMandante() : partida.getGolsVisitante();
            int golsClube2 = clube1Mandante ? partida.getGolsVisitante() : partida.getGolsMandante();

            if (clube1Jogou) {
                stats1[0] += golsClube1 > golsClube2 ? 1 : 0;
                stats1[1] += golsClube1 == golsClube2 ? 1 : 0;
                stats1[2] += golsClube1 < golsClube2 ? 1 : 0;
                stats1[3] += golsClube1;
                stats1[4] += golsClube2;

                stats2[0] += golsClube2 > golsClube1 ? 1 : 0;
                stats2[1] += golsClube2 == golsClube1 ? 1 : 0;
                stats2[2] += golsClube2 < golsClube1 ? 1 : 0;
                stats2[3] += golsClube2;
                stats2[4] += golsClube1;
            }

            listaPartidas.add(new ConfrontoPartidaDTO(
                    partida.getDataHora(),
                    partida.getEstadio().getNome(),
                    partida.getClubeMandante().getNome(),
                    partida.getClubeVisitante().getNome(),
                    partida.getGolsMandante(),
                    partida.getGolsVisitante()
            ));
        }

        return new ConfrontoDiretoDTO(
                listaPartidas,
                new ConfrontoResumoDTO(clube1.getNome(), stats1[0], stats1[1], stats1[2], stats1[3], stats1[4]),
                new ConfrontoResumoDTO(clube2.getNome(), stats2[0], stats2[1], stats2[2], stats2[3], stats2[4] )
        );
    }

    @Transactional(readOnly = true)
    public List<ClubeRankingDTO> ranking(String criterio) {
        List<Clube> clubes = clubeRepository.findAll();
        List<Partida> partidas = partidaRepository.findAll();

        Map<String, int[]> stats = new HashMap<>(); //Buca o clube trazendo (pontos, gols, vitórias e jogos)

        for (Partida partida : partidas) {
            String mandante = partida.getClubeMandante().getNome();
            String visitante = partida.getClubeVisitante().getNome();
            int gm = partida.getGolsMandante();
            int gv = partida.getGolsVisitante();

            //Mandante
            stats.putIfAbsent(mandante, new int[4]);
            stats.get(mandante)[1] += gm; //Apresenta os gols
            stats.get(mandante)[3] += 1; //Apresenta os jogos

            if (gm > gv) stats.get(mandante)[0] += 3; //Acrescenta os Pontos
            else if(gm == gv) stats.get(mandante)[0] += 1;

            if (gm >gv) stats.get(mandante)[2] += 1;

            //Visitantes
            stats.putIfAbsent(visitante, new int[4]);
            stats.get(visitante)[1] += gv;
            stats.get(visitante)[3] += 1;

            if (gv > gm) stats.get(visitante)[0] += 3;
            else if(gv == gm) stats.get(visitante)[0] += 1;

            if (gv > gm) stats.get(visitante)[2] += 1;
        }

        List<ClubeRankingDTO> lista = stats.entrySet().stream().map(
                e -> new ClubeRankingDTO(e.getKey(), e.getValue()[0], e.getValue()[1], e.getValue()[2], e.getValue()[3]))
                .filter(dto -> switch (criterio.toLowerCase()) {
                    case "pontos" -> dto.pontos() > 0;
                    case "gols" -> dto.gols() > 0;
                    case "vitorias" -> dto.vitorias() > 0;
                    case "jogos" -> dto.jogos() > 0;
                    default -> throw new BadRequestException("Critério inválido. Use: pontos, gols, vitorias, jogos");
                })
                .sorted(switch (criterio.toLowerCase()) {
                    case "pontos" -> Comparator.comparingInt(ClubeRankingDTO::pontos).reversed();
                    case "gols" -> Comparator.comparingInt(ClubeRankingDTO::gols).reversed();
                    case "vitorias" -> Comparator.comparingInt(ClubeRankingDTO::vitorias).reversed();
                    case "jogos" -> Comparator.comparingInt(ClubeRankingDTO::jogos).reversed();
                    default -> throw new BadRequestException("Critério inválido.");
                })
                .toList();

        return lista;
    }

    public Object editar(long l, ClubeUpdateDTO dto) {
        return null;
    }
}
