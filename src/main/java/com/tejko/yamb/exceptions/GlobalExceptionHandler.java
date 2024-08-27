package com.tejko.yamb.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import com.tejko.yamb.api.dto.responses.ErrorResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.util.CustomLogger;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final CustomLogger logger;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource, CustomLogger logger) {
        this.messageSource = messageSource;
        this.logger = logger;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        exception.printStackTrace();
        try {
            logger.error(exception);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exception) {       
        System.out.println(exception.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(MessageConstants.ERROR_USERNAME_OR_PASSWORD_INCORRECT);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ErrorResponse> handleException(AccessDeniedException exception) {       
        System.out.println(exception.getLocalizedMessage());
        logger.error(exception);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException exception) {       
        System.out.println(exception.getLocalizedMessage());
        logger.error(exception);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler({ IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exception) {       
        System.out.println(exception.getLocalizedMessage());  
        logger.error(exception);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}