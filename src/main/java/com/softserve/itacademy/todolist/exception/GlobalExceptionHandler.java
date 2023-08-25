package com.softserve.itacademy.todolist.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler { // consider extending ResponseEntityExceptionHandler
    private Logger logger;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        logger.error("ERROR - "+ ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getBindingResult().toString());
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        logger.error("ERROR - "+ ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("ERROR - "+ ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // todo: add more handlers
}
