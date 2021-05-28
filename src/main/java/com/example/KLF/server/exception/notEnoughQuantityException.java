package com.example.KLF.server.exception;

public class notEnoughQuantityException extends  Exception {
    public notEnoughQuantityException(){
        super("there is no enough quantity of the book for sale.");
    }
}
