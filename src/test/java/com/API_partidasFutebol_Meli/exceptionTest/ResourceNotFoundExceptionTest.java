package com.API_partidasFutebol_Meli.exceptionTest;

import com.API_partidasFutebol_Meli.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceNotFoundExceptionTest {

    @Test
    void deveRetornarMensagem() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Não encontrado");
        assertEquals("Não encontrado", exception.getMessage());
    }
}
