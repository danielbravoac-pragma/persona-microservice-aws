package com.pragma.aws.persona.service.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message) {
        super(message);
    }
}
