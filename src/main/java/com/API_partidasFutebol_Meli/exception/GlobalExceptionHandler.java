package com.API_partidasFutebol_Meli.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RecursoDuplicadoException.class)
  public ResponseEntity<String> conflito(RecursoDuplicadoException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> erroGenerico(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + ex.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> notFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> badRequest(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}