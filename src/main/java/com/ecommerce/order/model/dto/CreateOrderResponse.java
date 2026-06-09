package com.ecommerce.order.model.dto;

import com.ecommerce.order.constants.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for order creation.
 */
public class CreateOrderResponse {

    private Long orderId;
    private Long customerId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;

    // Constructors
    public CreateOrderResponse() {}

    public CreateOrderResponse(Long orderId, Long customerId, OrderStatus status,
                               BigDecimal totalAmount, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
