package com.API_partidasFutebol_Meli.controller;

import com.API_partidasFutebol_Meli.dto.EstadioRequestDTO;
import com.API_partidasFutebol_Meli.dto.EstadioResponseDTO;
import com.API_partidasFutebol_Meli.service.EstadioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadios")
public class EstadioController {

    private final EstadioService service;

    public EstadioController(EstadioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EstadioResponseDTO> cadastrar(@RequestBody @Valid EstadioRequestDTO dto) {
        var response = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
