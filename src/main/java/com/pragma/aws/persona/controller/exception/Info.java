package com.pragma.aws.persona.controller.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record Info(
        Integer code,
        String message,
        String error,
        String errorFields
) {
    public Info(Integer code, String message) {
        this(code, message, "", "");
    }

    public Info(String error, String message, Integer code) {
        this(code, message, error, "");
    }
}
