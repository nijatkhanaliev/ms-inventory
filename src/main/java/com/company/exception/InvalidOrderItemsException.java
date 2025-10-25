package com.company.exception;

import lombok.Getter;

@Getter
public class InvalidOrderItemsException extends RuntimeException{
    private final String errorMessage;
    private final String errorCode;

    public InvalidOrderItemsException(String errorMessage , String errorCode){
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}

