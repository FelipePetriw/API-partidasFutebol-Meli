package com.API_partidasFutebol_Meli.controller;

import com.API_partidasFutebol_Meli.dto.partida.PartidaFiltroDTO;
import com.API_partidasFutebol_Meli.dto.partida.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.partida.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    private final PartidaService service;

    public PartidaController(PartidaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PartidaResponseDTO> cadastrar(@RequestBody @Valid PartidaRequestDTO dto) {
        var response = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PartidaRequestDTO dto) {
        var response = service.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> buscarPorId(@PathVariable Long id) {
        var response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PartidaResponseDTO>> listar(
        @RequestParam(required = false)
        Long clubeId,

        @RequestParam(required = false)
        Long estadioId,

        @RequestParam(required = false)
        Boolean goleada,

        @PageableDefault(size = 10, sort = "dataHora", direction = Sort.Direction.DESC)
        Pageable pageable
        ){

        PartidaFiltroDTO filtro = new PartidaFiltroDTO(clubeId, estadioId, goleada);
        var page = service.listar(filtro, pageable);
        return ResponseEntity.ok(page);
    }
}