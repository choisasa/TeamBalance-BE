package com.sparta.balance.global.handler.exception;

@Getter
public class CustomApiException extends RuntimeException {

    public CustomApiException(String message) {
        super(message);
    }
}
