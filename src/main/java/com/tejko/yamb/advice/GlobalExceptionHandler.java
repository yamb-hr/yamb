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

import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.models.payload.ErrorResponse;
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
        return new ResponseEntity<>(new ErrorResponse(exception.getClass().getSimpleName(), exception.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exception) {
        exception.printStackTrace(System.out);        
        return new ResponseEntity<>(new ErrorResponse(MessageConstants.ERROR_BAD_REQUEST, MessageConstants.ERROR_USERNAME_OR_PASSWORD_INCORRECT), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException exception) {
        logger.error(exception);
        return new ResponseEntity<>(new ErrorResponse(MessageConstants.ERROR_FORBIDDEN, exception.getLocalizedMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class, ResourceNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {
        exception.printStackTrace(System.out);     
        logger.error(exception);
        return new ResponseEntity<>(new ErrorResponse(MessageConstants.ERROR_BAD_REQUEST, exception.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }
    
}