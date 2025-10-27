package com.smart.smartLibraryWeb.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFound(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        // Favicon ve static resource hatalarını ignore et (404 döndür, log'lama)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Resource not found: " + ex.getResourcePath()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        // GERÇEK HATAYI LOGLAYALIM!
        log.error("❌ EXCEPTION CAUGHT: {}", ex.getClass().getName(), ex);
        log.error("❌ ERROR MESSAGE: {}", ex.getMessage());
        if (ex.getCause() != null) {
            log.error("❌ CAUSED BY: {}", ex.getCause().getMessage());
        }

        // Geliştirme aşamasında gerçek hatayı döndür
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "message", "Beklenmeyen bir hata oluştu.",
                    "error", ex.getClass().getSimpleName(),
                    "detail", ex.getMessage() != null ? ex.getMessage() : "No detail available",
                    "cause", ex.getCause() != null ? ex.getCause().getMessage() : "No cause"
                ));
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleServiceException(ServiceException ex) {
        return new ErrorResponse("SERVICE_UNAVAILABLE", ex.getMessage());
    }

    @Data
    static class ErrorResponse {
        private final String error;
        private final String message;
    }


}
