package com.javachallenge.basico.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception e, WebRequest request) {
        return new ResponseEntity<Object>("You don't have permission to access.", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ NullPointerException.class, NoSuchElementException.class })
    public ResponseEntity<Object> handleNullPointerException(
            Exception e, WebRequest request) {
        return new ResponseEntity<Object>("Resource not found.", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
