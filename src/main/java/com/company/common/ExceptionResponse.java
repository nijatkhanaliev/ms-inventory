package com.company.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
public class ExceptionResponse {
    @JsonFormat(shape = STRING, pattern = "dd/MM/yyyy HH-mm-ss")
    private LocalDateTime timestamp;
    private String errorMessage;
    private String errorCode;
    private Set<ValidationError> validationErrors;

    public ExceptionResponse(String errorMessage, String errorCode, Set<ValidationError> validationErrors) {
        this.timestamp = LocalDateTime.now();
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.validationErrors = validationErrors;
    }

    @Getter
    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
