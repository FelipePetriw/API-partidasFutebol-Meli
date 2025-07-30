package com.API_partidasFutebol_Meli.controller;

import com.API_partidasFutebol_Meli.dto.estadio.EstadioRequestDTO;
import com.API_partidasFutebol_Meli.dto.estadio.EstadioResponseDTO;
import com.API_partidasFutebol_Meli.service.EstadioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> editar(@PathVariable Long id, @RequestBody @Valid EstadioRequestDTO dto) {
        var response = service.editar(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioResponseDTO> buscarPorId(@PathVariable Long id) {
        var response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EstadioResponseDTO>> listar(
        @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        var response = service.listarTodos(pageable);
        return ResponseEntity.ok(response);
    }
}

