package com.ecommerce.order.exception;

/**
 * Exception thrown when order data is invalid.
 */
public class InvalidOrderException extends RuntimeException {
    
    public InvalidOrderException(String message) {
        super(message);
    }
    
    public InvalidOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
