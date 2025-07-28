package com.API_partidasFutebol_Meli.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RecursoDuplicadoException.class)
  public ResponseEntity<String> duplicateResource(RecursoDuplicadoException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> genericError(Exception ex) {
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

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<String> conflict(ConflictException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
    StringBuilder sb = new StringBuilder("Erro de validação: ");
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      sb.append(error.getField())
              .append(" - ")
              .append(error.getDefaultMessage())
              .append("; ");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
  }
}