package com.API_partidasFutebol_Meli.exceptionTest;

import com.API_partidasFutebol_Meli.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BadRequestExceptionTest {

    @Test
    void deveCriarBadRequestExceptionComMensagem() {
        String mensagem = "Erro de requisicao inválida";
        BadRequestException exception = new BadRequestException(mensagem);

        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void deveLancarBadRequestException() {
        String mensagem = "Dados incorretos";

        Exception thrown = assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException(mensagem);
        });
        assertEquals(mensagem, thrown.getMessage());
    }
}
