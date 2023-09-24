package com.allane.vehicleleasingservice.exceptions;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message){

        super(message);
    }
}
