package com.API_partidasFutebol_Meli.repository;

import com.API_partidasFutebol_Meli.entity.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {
    Optional<Estadio> findByNomeIgnoreCase(String nome);
}
