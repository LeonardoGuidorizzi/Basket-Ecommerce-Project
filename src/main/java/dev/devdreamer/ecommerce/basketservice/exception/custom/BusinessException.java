package dev.devdreamer.ecommerce.basketservice.exception.custom;

public class BusinessException extends RuntimeException {
    public BusinessException(String message){
        super(message);
    }
}
