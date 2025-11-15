package com.company.exception.handler;

import com.company.exception.InsufficientStockException;
import com.company.exception.NotFoundException;
import com.company.common.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("Validation exception happened, message {}", ex.getMessage());
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map((e) -> new ExceptionResponse.ValidationError(
                        e.getField(),
                        e.getDefaultMessage()
                ))
                .collect(Collectors.toSet());

        return ResponseEntity.status(BAD_REQUEST)
                .body(new ExceptionResponse(
                        "Validation failed",
                        "VALIDATION_ERROR",
                        errors
                ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotFoundException ex) {
        log.error("NotFoundException happened, message '{}'", ex.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                .body(
                        new ExceptionResponse(
                                ex.getErrorMessage(),
                                ex.getErrorCode(),
                                null
                        )
                );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(InsufficientStockException ex) {
        log.error("InsufficientStockException happened, message '{}'", ex.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                .body(
                        new ExceptionResponse(
                                ex.getErrorMessage(),
                                ex.getErrorCode(),
                                null
                        )
                );
    }

}
