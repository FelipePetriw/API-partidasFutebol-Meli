package com.API_partidasFutebol_Meli.exceptionTest;

import com.API_partidasFutebol_Meli.exception.BadRequestException;
import com.API_partidasFutebol_Meli.exception.GlobalExceptionHandler;
import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
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
}
