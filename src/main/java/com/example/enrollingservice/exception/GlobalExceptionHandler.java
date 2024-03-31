package com.example.enrollingservice.exception;

import com.example.enrollingservice.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidTokenException(InvalidTokenException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedException(UnauthorizedException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


}
