package me.vik.socksstorageapp.exception;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(String message) {
        super(message);
    }
}