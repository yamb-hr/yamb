package com.tejko.yamb.exceptions;

import java.time.Instant;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tejko.yamb.api.dto.responses.ErrorResponse;
import com.tejko.yamb.exceptions.custom.GameStateException;
import com.tejko.yamb.util.CustomLogger;
import com.tejko.yamb.util.I18nUtil;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final CustomLogger logger;
    private final I18nUtil i18nUtil;

    @Autowired
    public GlobalExceptionHandler(CustomLogger logger, I18nUtil i18nUtil) {
        this.logger = logger;
        this.i18nUtil = i18nUtil;
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class,
        BadCredentialsException.class,
        AccessDeniedException.class,
        ResourceNotFoundException.class,
        PersistenceException.class,
        UnsupportedOperationException.class,
        Exception.class
    })
    public final ResponseEntity<Object> handleCustomExceptions(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleBadRequest(ex, headers, status, request);
        } else if (ex instanceof BadCredentialsException) {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            return handleUnauthorized((BadCredentialsException) ex, headers, status, request);
        } else if (ex instanceof AccessDeniedException) {
            HttpStatus status = HttpStatus.FORBIDDEN;
            return handleForbidden((AccessDeniedException) ex, headers, status, request);
        } else if (ex instanceof ResourceNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleNotFound((ResourceNotFoundException) ex, headers, status, request);
        } else if (ex instanceof UnsupportedOperationException) {
            HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
            return handleUnsupported((UnsupportedOperationException) ex, headers, status, request);
        } else {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleInternalServerError(ex, headers, status, request);
        }
    }    

    protected ResponseEntity<Object> handleBadRequest(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex);
        ErrorResponse errorResponse = createErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        System.out.println("---------------------------------------------------\n------------------------------------------------");
        logException(ex);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();
        ErrorResponse errorResponse = createErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    protected ResponseEntity<Object> handleUnauthorized(BadCredentialsException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex);
        ErrorResponse errorResponse = createErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    protected ResponseEntity<Object> handleForbidden(AccessDeniedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex);
        ErrorResponse errorResponse = createErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex);
        ErrorResponse errorResponse = createErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    protected ResponseEntity<Object> handleUnsupported(UnsupportedOperationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex);
        ErrorResponse errorResponse = createErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    protected ResponseEntity<Object> handleInternalServerError(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logException(ex);
        ErrorResponse errorResponse = createErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    private void logException(Exception ex) {
        try {
            logger.error(ex);
        } catch (Exception e) {
            ex.printStackTrace();
            System.out.println("Logging failed: " + e.getLocalizedMessage());
        }
    }

    private ErrorResponse createErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(status.value());
        
        if (ex instanceof GameStateException) {
            GameStateException gameStateException = (GameStateException) ex;
            errorResponse.setMessage(i18nUtil.getMessage(gameStateException.getMessageKey(), gameStateException.getArgs()));
        } else {
            errorResponse.setMessage(i18nUtil.getMessage(ex.getLocalizedMessage()));
        }
    
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setTimestamp(Instant.now());
        
        return errorResponse;
    }
}