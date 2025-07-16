package com.API_partidasFutebol_Meli.repository;

import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.entity.Partida;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface PartidaRepository extends JpaRepository<Partida, Long>, JpaSpecificationExecutor<Partida> {

    boolean existsByClubeMandanteOrClubeVisitanteAndDataHoraBetween(
            Clube clubeMandante, Clube clubeVisitante,
            LocalDateTime inicio, LocalDateTime fim
    );

    boolean existsByEstadioAndDataHoraBetween(
            Estadio estadio, LocalDateTime inicio, LocalDateTime fim
    );
}
