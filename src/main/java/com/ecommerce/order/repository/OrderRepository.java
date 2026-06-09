package com.ecommerce.order.repository;

import com.ecommerce.order.constants.OrderStatus;
import com.ecommerce.order.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Order entity.
 * Maps from legacy OrderDAO.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find orders by customer ID.
     * 
     * @param customerId Customer ID
     * @return List of orders
     */
    List<Order> findByCustomerId(Long customerId);
    
    /**
     * Find orders by status.
     * 
     * @param status Order status
     * @return List of orders
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * Find orders by customer ID and status.
     * 
     * @param customerId Customer ID
     * @param status Order status
     * @return List of orders
     */
    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);
}
