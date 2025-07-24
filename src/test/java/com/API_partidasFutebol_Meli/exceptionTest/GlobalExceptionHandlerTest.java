package com.API_partidasFutebol_Meli.exceptionTest;

import com.API_partidasFutebol_Meli.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveLidarComRecursosNaoEncontrado() {
        ResponseEntity<String> response = handler.notFound(new ResourceNotFoundException("Faltando"));
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Faltando", response.getBody());
    }

    @Test
    void deveLidarComRecursoNaoEncontrado() {
        ResponseEntity<String> response = handler.badRequest(new BadRequestException("ruim"));
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("ruim", response.getBody());
    }

    @Test
    void deveLidarComRecursoDuplicado() {
        ResponseEntity<String> response = handler.duplicateResource(new RecursoDuplicadoException("Duplicado"));
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Duplicado", response.getBody());
    }

    @Test
    void deveLidarComConflito() {
        ResponseEntity<String> response = handler.conflict(new ConflictException("Conflito detectado"));
        assertEquals(409, response.getStatusCodeValue());
        assertEquals("Conflito detectado", response.getBody());
    }

    @Test
    void deveLidarComErroGenerico() {
        ResponseEntity<String> response = handler.genericError(new RuntimeException("Falha Interna"));
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erro inesperado: Falha Interna", response.getBody());
    }
}
