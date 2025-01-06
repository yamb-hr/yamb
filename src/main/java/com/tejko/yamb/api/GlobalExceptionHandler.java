package com.tejko.yamb.api;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

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
import com.tejko.yamb.util.I18nUtil;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final I18nUtil i18nUtil;
    private final AtomicLong errorCount = new AtomicLong();

    @Autowired
    public GlobalExceptionHandler(I18nUtil i18nUtil) {
        this.i18nUtil = i18nUtil;
    }

    public long getErrorCount() {
        return errorCount.get();
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
        BadCredentialsException.class,
        AccessDeniedException.class,
        ResourceNotFoundException.class,
        IllegalStateException.class,
        PersistenceException.class,
        UnsupportedOperationException.class,
        Exception.class
    })
    public final ResponseEntity<Object> handleCustomExceptions(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        errorCount.incrementAndGet();

        HttpStatus status;
        if (ex instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof IllegalStateException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof PersistenceException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof UnsupportedOperationException) {
            status = HttpStatus.NOT_IMPLEMENTED;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        logger.error("Internal Server Error", ex);
        ErrorResponse errorResponse = createErrorResponse(ex, status, request);
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder messageBuilder = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String messageKey = error.getDefaultMessage();
            String errorMessage = i18nUtil.getMessage(messageKey);

            if (messageBuilder.length() > 0) {
                messageBuilder.append(", ");
            }
            messageBuilder.append(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(messageBuilder.toString().trim());
        errorResponse.setTimestamp(Instant.now());

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    private ErrorResponse createErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        return errorResponse;
    }
}
