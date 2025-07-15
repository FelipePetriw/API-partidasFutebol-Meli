package com.API_partidasFutebol_Meli.repository;

import com.API_partidasFutebol_Meli.entity.Clube;
import com.API_partidasFutebol_Meli.entity.Estadio;
import com.API_partidasFutebol_Meli.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    boolean existsByClubeMandanteOrClubeVisitanteAndDataHoraBetween(
            Clube clubeMandante, Clube clubeVisitante,
            LocalDateTime inicio, LocalDateTime fim
    );

    boolean existsByEstadioAndDataHoraBetween(
            Estadio estadio, LocalDateTime inicio, LocalDateTime fim
    );
}
