package com.tejko.yamb.advice;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.tejko.yamb.api.payload.responses.ErrorResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.util.Logger;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    Logger logger;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        exception.printStackTrace(System.out);
        logger.error(exception);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = exception.getLocalizedMessage();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exception) {
        exception.printStackTrace(System.out);    
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = MessageConstants.ERROR_USERNAME_OR_PASSWORD_INCORRECT;
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException exception) {
        logger.error(exception);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = exception.getLocalizedMessage();
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException exception) {
        exception.printStackTrace(System.out);     
        logger.error(exception);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = exception.getLocalizedMessage();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
        exception.printStackTrace(System.out);     
        logger.error(exception);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.message = exception.getLocalizedMessage();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}