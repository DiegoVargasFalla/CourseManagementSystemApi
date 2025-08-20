package com.ubbackend.exception;

public class ResourceNotCreatedException extends Exception{
    public ResourceNotCreatedException(String message) {
        super(message);
    }

    public ResourceNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
