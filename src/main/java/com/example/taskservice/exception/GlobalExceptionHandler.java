package com.example.taskservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TaskNotFoundException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleResourceNotFoundException(TaskNotFoundException unfe, ServerWebExchange exchange) {

        ErrorResponse errorDetails = getErrorDetails(unfe, exchange);

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails));
    }

    @ExceptionHandler({BadRequestException.class})
    public Mono<ResponseEntity<ErrorResponse>> handleBadRequestException(BadRequestException bre, ServerWebExchange exchange) {

        ErrorResponse errorDetails = getErrorDetails(bre, exchange);

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInternalServerError(Exception ex, ServerWebExchange exchange) {

        ErrorResponse errorDetails = getErrorDetails(ex, exchange);

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails));
    }

    private ErrorResponse getErrorDetails(Exception e, ServerWebExchange exchange) {
        return new ErrorResponse(
                LocalDateTime.now(),
                e.getMessage(),
                exchange.getRequest().getPath().value());
    }
}
