package com.ecommerce.order.model.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event consumed from Order Service when new order is created.
 * Triggers automatic creation of order history entry.
 */
public class OrderCreatedEvent {

    private Long orderId;
    private Long customerId;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String orderStatus;
    private Long timestamp;
    private LocalDateTime orderDate;

    // Constructors
    public OrderCreatedEvent() {}

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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getOrderDate(LocalDateTime now) {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
