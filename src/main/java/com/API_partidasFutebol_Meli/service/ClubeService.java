package com.API_partidasFutebol_Meli.service;

import com.API_partidasFutebol_Meli.dto.ClubeFiltroDTO;
import com.API_partidasFutebol_Meli.dto.ClubeRequestDTO;
import com.API_partidasFutebol_Meli.dto.ClubeResponseDTO;
import com.API_partidasFutebol_Meli.dto.ClubeUpdateDTO;
import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import com.API_partidasFutebol_Meli.repository.ClubeRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Transactional
    public void inativar(Long id) {
        Clube clube = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Clube não encontrado."));

        clube.setAtivo(false);
        repository.save(clube);
    }

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

    public Page<ClubeResponseDTO> listar(ClubeFiltroDTO filtro, Pageable pageable) {
        Specification<Clube> spec = Specification.where(null);

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
}
