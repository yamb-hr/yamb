package com.tejko.advice;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tejko.constants.GameConstants;
import com.tejko.models.payload.ErrorResponse;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        exception.printStackTrace(System.out);
        return new ResponseEntity<>(new ErrorResponse(exception.getClass().getSimpleName(), exception.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadCredentialsException.class, IllegalArgumentException.class, IllegalStateException.class, ResourceNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
        return new ResponseEntity<>(new ErrorResponse(GameConstants.ERROR_BAD_REQUEST, exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException exception) {
        return new ResponseEntity<>(new ErrorResponse(GameConstants.ERROR_FORBIDDEN, exception.getLocalizedMessage()), HttpStatus.FORBIDDEN);
    }
    
    
}