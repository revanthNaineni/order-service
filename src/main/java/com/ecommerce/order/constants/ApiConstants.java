package com.ecommerce.order.constants;

/**
 * API constants for Order Service.
 */
public final class ApiConstants {
    
    private ApiConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String API_BASE_PATH = "/api/orders";
    public static final String API_VERSION = "v1";
    
    // HTTP Headers
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    
    // Error Messages
    public static final String ORDER_NOT_FOUND = "Order not found with ID: %s";
    public static final String INVALID_ORDER_DATA = "Invalid order data provided";
    public static final String ORDER_CREATION_FAILED = "Failed to create order";
}
