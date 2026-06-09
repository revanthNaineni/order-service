package com.ecommerce.order.repository;

import com.ecommerce.order.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for OrderItem entity.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    /**
     * Find order items by order ID.
     * 
     * @param orderId Order ID
     * @return List of order items
     */
    List<OrderItem> findByOrder_OrderId(Long orderId);
}
