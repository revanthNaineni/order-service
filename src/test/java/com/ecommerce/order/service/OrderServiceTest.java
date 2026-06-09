package com.ecommerce.order.service;

import com.ecommerce.order.constants.OrderStatus;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.model.dto.*;
import com.ecommerce.order.model.entity.Order;
import com.ecommerce.order.model.entity.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaEventPublisher kafkaEventPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    private CreateOrderRequest createOrderRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        OrderItemDTO itemDTO = new OrderItemDTO("ITEM123", 2, new BigDecimal("29.99"));
        
        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setCustomerId(123L);
        createOrderRequest.setItems(Arrays.asList(itemDTO));

        order = new Order();
        order.setOrderId(1L);
        order.setCustomerId(123L);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("59.98"));
        
        OrderItem item = new OrderItem("ITEM123", 2, new BigDecimal("29.99"));
        order.addItem(item);
    }

    @Test
    void testCreateOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        CreateOrderResponse response = orderService.createOrder(createOrderRequest);

        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals(123L, response.getCustomerId());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertEquals(new BigDecimal("59.98"), response.getTotalAmount());

        verify(orderRepository, times(1)).save(any(Order.class));
        //verify(kafkaEventPublisher, times(1)).publishOrderCreatedEvent(any(Order.class));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getOrderId());
        assertEquals(123L, result.getCustomerId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(999L);
        });

        verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.updateOrderStatus(1L, OrderStatus.SHIPPED);

        assertNotNull(result);
        assertEquals(OrderStatus.SHIPPED, result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testProcessReturn_Success() {
        order.setStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.processReturn(1L);

        assertNotNull(result);
        assertEquals(OrderStatus.RETURNED, result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
     }
}
