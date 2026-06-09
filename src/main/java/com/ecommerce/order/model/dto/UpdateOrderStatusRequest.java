package com.ecommerce.order.model.dto;

import com.ecommerce.order.constants.OrderStatus;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating order status.
 */
public class UpdateOrderStatusRequest {

    @NotNull(message = "Status is required")
    private OrderStatus status;

    // Constructors
    public UpdateOrderStatusRequest() {}

    public UpdateOrderStatusRequest(OrderStatus status) {
        this.status = status;
    }

    // Getters and Setters
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
