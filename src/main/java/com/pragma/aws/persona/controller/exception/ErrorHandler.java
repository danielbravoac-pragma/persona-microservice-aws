package com.pragma.aws.persona.controller.exception;

import com.pragma.aws.persona.service.exception.DataAlreadyExists;
import com.pragma.aws.persona.service.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleDefaultException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.INTERNAL_SERVER_ERROR.value(), MessagesController.SOMETHING_HAPPEN_CONTACT_IT_TEAM.name())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining(","));
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), MessagesController.SOME_INVALID_FIELDS.name(), "BAD REQUEST", message)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GenericResponse> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.NOT_FOUND.value(), MessagesController.SOME_DATA_COULD_NOT_BE_FOUND.name())), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataAlreadyExists.class)
    public ResponseEntity<GenericResponse> handleDataAlreadyExistsException(DataAlreadyExists ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), MessagesController.SOME_DATA_IS_ALREADY_REGISTERED.name())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GenericResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new GenericResponse<>(new Info(HttpStatus.BAD_REQUEST.value(), MessagesController.SOME_PARAMETERS_ARE_MISSING.name())), HttpStatus.BAD_REQUEST);
    }
}
