package com.allane.vehicleleasingservice;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message){

        super(message);
    }
}
