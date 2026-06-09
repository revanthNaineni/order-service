package com.ecommerce.order.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Request DTO for creating a new order.
 * Maps from legacy OrderVO input.
 */
public class CreateOrderRequest {

    private Long customerId;
    
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemDTO> items;

    // Constructors
    public CreateOrderRequest() {}

    public CreateOrderRequest(Long customerId, List<OrderItemDTO> items) {
        this.customerId = customerId;
        this.items = items;
    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
