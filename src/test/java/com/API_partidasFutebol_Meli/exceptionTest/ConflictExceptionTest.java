package com.API_partidasFutebol_Meli.exceptionTest;

import com.API_partidasFutebol_Meli.exception.ConflictException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConflictExceptionTest {

    @Test
    void deveRetornarMensagemDeErro() {
        ConflictException ex = new ConflictException("Conflito detectado");
        assertEquals("Conflito detectado", ex.getMessage());
    }
}
