package com.softserve.itacademy.todolist.exception;

public class MethodArgumentNotValidException extends RuntimeException {
    public MethodArgumentNotValidException() {    }

    public MethodArgumentNotValidException(String message) {
        super(message);
    }
}
