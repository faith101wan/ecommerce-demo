package com.cw.ecommerce.config;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}