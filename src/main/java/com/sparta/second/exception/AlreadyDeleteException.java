package com.sparta.second.exception;

public class AlreadyDeleteException extends RuntimeException {
    public AlreadyDeleteException(String message) {
        super(message);
    }
}
