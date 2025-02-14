package com.ampersand.groom.global.error.handler;

import com.ampersand.groom.global.error.data.response.ErrorResponse;
import com.ampersand.groom.global.error.exception.GroomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GroomException.class)
    public ResponseEntity<ErrorResponse> handleGroomException(GroomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new ErrorResponse(e.getErrorCode().getMessage(), e.getErrorCode().getHttpStatus()));
    }
}