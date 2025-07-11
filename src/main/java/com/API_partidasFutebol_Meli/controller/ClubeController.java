package com.API_partidasFutebol_Meli.controller;

import com.API_partidasFutebol_Meli.dto.ClubeRequestDTO;
import com.API_partidasFutebol_Meli.dto.ClubeResponseDTO;
import com.API_partidasFutebol_Meli.dto.ClubeUpdateDTO;
import com.API_partidasFutebol_Meli.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
}
