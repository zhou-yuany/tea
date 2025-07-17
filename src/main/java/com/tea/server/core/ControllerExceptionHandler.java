package com.tea.server.core;

import cn.hutool.core.exceptions.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
class ControllerExceptionHandler {

    @Autowired
    private HttpServletRequest request;

    /**
     * Exception Handler for common Exceptions.
     *
     * @param e The Exception.
     * @return ResponseEntity containing the ErrorModel and status.
     */
    @ExceptionHandler({Exception.class, NullPointerException.class, InvocationTargetException.class,
            ClassCastException.class, UnsupportedOperationException.class})
    public ResponseEntity<Object> handleNullException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResult(e.getMessage()));
    }

    /**
     * Exception Handler for when the spring boot api fails to send a request to another application.
     *
     * @param e The Exception.
     * @return ResponseEntity containing the ErrorModel and status.
     */
    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<Object> handleHttpServerErrorException(Exception e) {
        log.error(e.getMessage(), e);
        String errorMessage = e.getMessage();
        if (e instanceof HttpStatusCodeException) {
            String errorResponseBody = ((HttpStatusCodeException) e).getResponseBodyAsString();
            errorMessage = String.format("REST client received error response: %s", errorResponseBody);
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResult(errorMessage));
    }

    /**
     * Exception Handler for common bad request type Exceptions.
     *
     * @param e The Exception.
     * @return ResponseEntity containing the ErrorModel and status.
     */
    @ExceptionHandler({ConversionFailedException.class, ValidateException.class, BindException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleMiscFailures(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult(e.getMessage()));
    }

    /**
     * Exception Handler for the unsupported method.
     *
     * @param e The Exception.
     * @return ResponseEntity containing the ErrorModel and status.
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleMethodNotSupported(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResult(e.getMessage()));
    }

    /**
     * Fallback for Spring Validation GET errors with @RequestParam required.
     * MethodArgumentNotValidException thrown when the request is
     * missing a parameter.
     *
     * @param e The MethodArgumentNotValidException.
     * @return ResponseEntity containing the ErrorModel and status.
     */
    @ExceptionHandler({MissingServletRequestParameterException.class, ServletRequestBindingException.class})
    protected ResponseEntity<Object> handleMissingRequestParam(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResult(e.getMessage()));
    }

    /**
     * Exception Handler for HttpMediaTypeNotSupportedException.
     *
     * @param e The HttpMediaTypeNotSupportedException exception.
     * @return ResponseEntity containing the ErrorModel and status.
     */
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResult(e.getMessage()));
    }


}
