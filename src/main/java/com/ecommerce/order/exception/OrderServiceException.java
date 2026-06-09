package com.ecommerce.order.exception;

/**
 * Generic exception for Order Service errors.
 * Maps from legacy BusinessDelegateException.
 */
public class OrderServiceException extends RuntimeException {
    
    private final String errorCode;
    
    public OrderServiceException(String message) {
        super(message);
        this.errorCode = "ORDER_SERVICE_ERROR";
    }
    
    public OrderServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public OrderServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ORDER_SERVICE_ERROR";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
