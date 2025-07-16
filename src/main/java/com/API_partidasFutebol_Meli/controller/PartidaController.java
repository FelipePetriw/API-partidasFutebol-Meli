package com.API_partidasFutebol_Meli.controller;

import com.API_partidasFutebol_Meli.dto.PartidaRequestDTO;
import com.API_partidasFutebol_Meli.dto.PartidaResponseDTO;
import com.API_partidasFutebol_Meli.service.PartidaService;
import jakarta.validation.Valid;
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
}
