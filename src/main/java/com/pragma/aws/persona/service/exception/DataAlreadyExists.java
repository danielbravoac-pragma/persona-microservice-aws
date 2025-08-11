package com.pragma.aws.persona.service.exception;

public class DataAlreadyExists extends RuntimeException {
    public DataAlreadyExists(String message) {
        super(message);
    }
}
