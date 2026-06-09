package com.ecommerce.order.repository;

import com.ecommerce.order.constants.OrderStatus;
import com.ecommerce.order.model.entity.Order;
import com.ecommerce.order.model.entity.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for OrderRepository.
 */
@DataJpaTest
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",  // ADDED: Disable Flyway
})
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setCustomerId(123L);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("59.98"));

        try {
            java.lang.reflect.Field createdAtField = Order.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(order, LocalDateTime.now());

            java.lang.reflect.Field updatedAtField = Order.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(order, LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException("Failed to set audit fields", e);
        }

        OrderItem item = new OrderItem("ITEM123", 2, new BigDecimal("29.99"));
        order.addItem(item);

        entityManager.persist(order);
        entityManager.flush();
    }

    @Test
    void testFindByCustomerId() {
        List<Order> orders = orderRepository.findByCustomerId(123L);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(123, orders.get(0).getCustomerId());
    }

    @Test
    void testFindByStatus() {
        List<Order> orders = orderRepository.findByStatus(OrderStatus.PENDING);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(OrderStatus.PENDING, orders.get(0).getStatus());
    }

    @Test
    void testFindByCustomerIdAndStatus() {
        List<Order> orders = orderRepository.findByCustomerIdAndStatus(
            123L, OrderStatus.PENDING);

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(123, orders.get(0).getCustomerId());
        assertEquals(OrderStatus.PENDING, orders.get(0).getStatus());
    }
}
