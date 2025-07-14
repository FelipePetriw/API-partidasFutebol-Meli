package com.API_partidasFutebol_Meli.repository;

import com.API_partidasFutebol_Meli.entity.Clube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClubeRepository extends JpaRepository<Clube, Long>, JpaSpecificationExecutor<Clube> {
    Optional<Clube> findByNomeAndSiglaEstado(String nome, String siglaEstado);
}
