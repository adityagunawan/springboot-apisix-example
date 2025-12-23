package com.example.userservice.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<StandardResponse<Object>> handleResponseStatus(ResponseStatusException ex) {
    int status = ex.getStatusCode().value();
    ErrorDetail error = new ErrorDetail(ex.getStatusCode().toString(), ex.getReason(), null);
    return ResponseEntity
        .status(status)
        .body(StandardResponse.error(status, ex.getReason() != null ? ex.getReason() : "error", error));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<StandardResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
    int status = HttpStatus.BAD_REQUEST.value();
    ErrorDetail error = new ErrorDetail("BAD_REQUEST", "Validation failed", ex.getBindingResult().getAllErrors());
    return ResponseEntity
        .status(status)
        .body(StandardResponse.error(status, "Validation failed", error));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<StandardResponse<Object>> handleGeneric(Exception ex) {
    int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    ErrorDetail error = new ErrorDetail("INTERNAL_ERROR", ex.getMessage(), null);
    return ResponseEntity
        .status(status)
        .body(StandardResponse.error(status, "Internal server error", error));
  }
}
