package com.API_partidasFutebol_Meli.exceptionTest;

import com.API_partidasFutebol_Meli.exception.RecursoDuplicadoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecursoDuplicadoExceptionTest {

    @Test
    void deveRetornarMensagemDeErro() {
        RecursoDuplicadoException ex = new RecursoDuplicadoException("Duplicado");
        assertEquals("Duplicado", ex.getMessage());
    }
}
