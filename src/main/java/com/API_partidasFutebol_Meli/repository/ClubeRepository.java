package com.API_partidasFutebol_Meli.repository;

import com.API_partidasFutebol_Meli.entity.Clube;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubeRepository extends JpaRepository<Clube, Long> {
    Optional<Clube> findByNomeAndSiglaEstado(String nome, String siglaEstado);
}
