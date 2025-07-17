package com.API_partidasFutebol_Meli.controller;

import com.API_partidasFutebol_Meli.dto.*;
import com.API_partidasFutebol_Meli.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    private ClubeService service;

    public ClubeController(ClubeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criar(@RequestBody @Valid ClubeRequestDTO dto) {
        var response = service.criar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/id")
    public ResponseEntity<ClubeResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClubeUpdateDTO dto) {
        var response = service.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeResponseDTO> buscarPorId(@PathVariable Long id) {
        var response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ClubeResponseDTO>> listar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String siglaEstado,
        @RequestParam(required = false) Boolean ativo,
        @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        var filtros = new ClubeFiltroDTO(nome, siglaEstado, ativo);
        var page = service.listar(filtros, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<RetrospectoDTO> retrospectoGeral(@PathVariable Long id) {
        var response = service.retrospectoGeral(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/retrospecto/adversarios")
    public ResponseEntity<List<RetrospectoAdversarioDTO>> retrospectoPorAdversario(@PathVariable Long id) {
        var response = service.retrospectoPorAdversario(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confronto")
    public ResponseEntity<ConfrontoDiretoDTO> confronto(
            @RequestParam Long clube1,
            @RequestParam Long clube2) {
        var response = service.confrontoPorDireto(clube1, clube2);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<ClubeRankingDTO>> ranking(@RequestParam String criterio) {
        var response = service.ranking(criterio);
        return ResponseEntity.ok(response);
    }
}
