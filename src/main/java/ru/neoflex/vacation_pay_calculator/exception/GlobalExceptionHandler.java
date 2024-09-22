package ru.neoflex.vacation_pay_calculator.exception;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getDefaultMessage());
        }

        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Validation failed")
                .message("One or more fields have validation errors.")
                .errors(errors)
                .build();
    }

    @ExceptionHandler({JsonParseException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleJsonException(Exception ex) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Invalid JSON format")
                .message("The request body is not a valid JSON structure.")
                .errors(List.of(ex.getMessage()))
                .build();
    }

    @Builder
    @Getter
    public static class ApiError {
        private HttpStatus status;
        private String reason;
        private String message;

        private List<String> errors;

        @Builder.Default
        private LocalDateTime timestamp = LocalDateTime.now();
    }
}