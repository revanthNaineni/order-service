package com.ecommerce.order.constants;

/**
 * Order status enumeration.
 * Maps from legacy order status workflow.
 */
public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    RETURNED("Returned"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
