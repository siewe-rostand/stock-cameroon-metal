package com.siewe.inventorymanagementsystem.utils;

public class CustomErrorType extends Throwable {

    private String errorMessage;

    public CustomErrorType(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
