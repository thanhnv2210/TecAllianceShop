package com.tecalliance.shop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<CustomApiError> exception(CustomException exception) {
        return buildResponseEntity(
                new CustomApiError(exception.status,exception.getMessage()));
    }

    private ResponseEntity<CustomApiError> buildResponseEntity(CustomApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
