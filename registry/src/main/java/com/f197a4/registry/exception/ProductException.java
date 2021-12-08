package com.f197a4.registry.exception;

public class ProductException extends RuntimeException {
    public ProductException(Long id, String cause) {
        super("There was a problem with product ("+id+"): "+cause);
    }
}
