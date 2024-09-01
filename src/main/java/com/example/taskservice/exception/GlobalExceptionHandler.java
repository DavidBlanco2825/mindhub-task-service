package com.example.taskservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TaskNotFoundException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleResourceNotFoundException(TaskNotFoundException unfe, ServerWebExchange exchange) {

        ErrorResponse errorDetails = getErrorDetails(unfe.getMessage(), exchange);

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails));
    }

    @ExceptionHandler({BadRequestException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleBadRequestException(BadRequestException bre, ServerWebExchange exchange) {

        ErrorResponse errorDetails = getErrorDetails(bre.getMessage(), exchange);

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationExceptions(WebExchangeBindException ex, ServerWebExchange exchange) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(objectError -> {
                    if (objectError instanceof FieldError) {
                        return ((FieldError) objectError).getField() + ": " + objectError.getDefaultMessage();
                    } else {
                        return objectError.getObjectName() + ": " + objectError.getDefaultMessage();
                    }
                })
                .collect(Collectors.joining(", "));

        ErrorResponse errorDetails = getErrorDetails(errorMessage, exchange);

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInternalServerError(Exception ex, ServerWebExchange exchange) {

        ErrorResponse errorDetails = getErrorDetails(ex.getMessage(), exchange);

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails));
    }

    private ErrorResponse getErrorDetails(String message, ServerWebExchange exchange) {
        return new ErrorResponse(
                LocalDateTime.now(),
                message,
                exchange.getRequest().getPath().value());
    }
}
