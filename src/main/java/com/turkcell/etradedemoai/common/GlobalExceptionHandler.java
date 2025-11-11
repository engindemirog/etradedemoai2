package com.turkcell.etradedemoai.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all controllers
 * Handles different exception types and returns RFC 7807 compliant responses
 * TEMPORARILY DISABLED - uncomment @RestControllerAdvice when springdoc issue is resolved
 */
//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessProblemDetail> handleBusinessException(
            BusinessException ex, 
            HttpServletRequest request) {
        
        BusinessProblemDetail problemDetail = new BusinessProblemDetail();
        problemDetail.setType("https://etradedemo.com/errors/business");
        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(request.getRequestURI());
        problemDetail.setBusinessCode(ex.getBusinessCode());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(error.getField(), error.getDefaultMessage());
        }
        
        CustomProblemDetail problemDetail = new CustomProblemDetail();
        problemDetail.setType("https://etradedemo.com/errors/validation");
        problemDetail.setTitle("Validation Failed");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail("One or more fields have validation errors: " + validationErrors.toString());
        problemDetail.setInstance(request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        
        CustomProblemDetail problemDetail = new CustomProblemDetail();
        problemDetail.setType("https://etradedemo.com/errors/illegal-argument");
        problemDetail.setTitle("Invalid Argument");
        problemDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomProblemDetail> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {
        
        CustomProblemDetail problemDetail = new CustomProblemDetail();
        problemDetail.setType("https://etradedemo.com/errors/runtime");
        problemDetail.setTitle("Runtime Error");
        problemDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomProblemDetail> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        CustomProblemDetail problemDetail = new CustomProblemDetail();
        problemDetail.setType("https://etradedemo.com/errors/internal");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problemDetail.setDetail("An unexpected error occurred. Please contact support.");
        problemDetail.setInstance(request.getRequestURI());
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }
}
