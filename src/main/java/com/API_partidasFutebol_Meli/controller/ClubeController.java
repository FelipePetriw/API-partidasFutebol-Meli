package com.API_partidasFutebol_Meli.controller;

import com.API_partidasFutebol_Meli.dto.ClubeRequestDTO;
import com.API_partidasFutebol_Meli.dto.ClubeResponseDTO;
import com.API_partidasFutebol_Meli.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    private ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    public ResponseEntity<ClubeResponseDTO> criar(@Valid @RequestBody ClubeRequestDTO dto) {
        var response = service.criar(dto);
        return ResponseEntity.status(201).body(response);
    }
}
