package com.ecommerce.order.service;

import com.ecommerce.order.constants.OrderStatus;
import com.ecommerce.order.model.dto.CreateOrderRequest;
import com.ecommerce.order.model.dto.CreateOrderResponse;
import com.ecommerce.order.model.dto.OrderDTO;

/**
 * Service interface for Order operations.
 * Maps from legacy OrderDelegate and OrderService.
 */
public interface OrderService {
    
    /**
     * Create a new order.
     * Maps from: OrderDelegate.createOrder(orderVO)
     * 
     * @param request Order creation request
     * @return Created order response
     */
    CreateOrderResponse createOrder(CreateOrderRequest request);
    
    /**
     * Get order by ID.
     * Maps from: OrderDelegate.getOrderStatus(orderId)
     * 
     * @param orderId Order ID
     * @return Order details
     */
    OrderDTO getOrderById(Long orderId);
    
    /**
     * Update order status.
     * Maps from: OrderDelegate.updateOrderStatus(orderId, status)
     * 
     * @param orderId Order ID
     * @param status New status
     * @return Updated order
     */
    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
    
    /**
     * Process order return.
     * Maps from: OrderDelegate.processReturn(orderId)
     * 
     * @param orderId Order ID
     * @return Updated order with RETURNED status
     */
    OrderDTO processReturn(Long orderId);
}
