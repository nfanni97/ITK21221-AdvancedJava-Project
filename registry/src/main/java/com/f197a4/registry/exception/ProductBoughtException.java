package com.f197a4.registry.exception;

public class ProductBoughtException extends RuntimeException {
    public ProductBoughtException(Long productId, Long recipientId) {
        super("Product with id "+productId+" is already bought for user with id "+recipientId);
    }
}
