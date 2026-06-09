package com.ecommerce.order.service;

import com.ecommerce.order.constants.ApiConstants;
import com.ecommerce.order.constants.OrderStatus;
import com.ecommerce.order.exception.InvalidOrderException;
import com.ecommerce.order.exception.OrderNotFoundException;
import com.ecommerce.order.model.dto.*;
import com.ecommerce.order.model.entity.Order;
import com.ecommerce.order.model.entity.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Implementation of OrderService.
 * Maps from legacy OrderDelegate and OrderService.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository, 
                           KafkaEventPublisher kafkaEventPublisher) {
        this.orderRepository = orderRepository;
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        logger.info("Creating order for customer: {}", request.getCustomerId());

        try {
            // Create order entity
            Order order = new Order();
            order.setCustomerId(request.getCustomerId());
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);

            // Add order items and calculate total
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderItemDTO itemDTO : request.getItems()) {
                OrderItem item = new OrderItem(
                    itemDTO.getItemId(),
                    itemDTO.getQuantity(),
                    itemDTO.getPrice()
                );
                order.addItem(item);
                
                BigDecimal itemTotal = itemDTO.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }
            order.setTotalAmount(totalAmount);

            // Save order
            Order savedOrder = orderRepository.save(order);
            logger.info("Order created successfully with ID: {}", savedOrder.getOrderId());

            // Publish OrderCreatedEvent to Kafka
            kafkaEventPublisher.publishOrderCreatedEvent(savedOrder);

            // Return response
            return new CreateOrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerId(),
                savedOrder.getStatus(),
                savedOrder.getTotalAmount(),
                savedOrder.getOrderDate()
            );

        } catch (Exception e) {
            logger.error("Error creating order for customer: {}", request.getCustomerId(), e);
            throw new InvalidOrderException(ApiConstants.ORDER_CREATION_FAILED, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) {
        logger.info("Retrieving order with ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(
                String.format(ApiConstants.ORDER_NOT_FOUND, orderId)));

        return mapToDTO(order);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        logger.info("Updating order {} status to {}", orderId, status);

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(
                String.format(ApiConstants.ORDER_NOT_FOUND, orderId)));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        kafkaEventPublisher.publishOrderCreatedEvent(updatedOrder);
        logger.info("Order {} status updated to {}", orderId, status);

        return mapToDTO(updatedOrder);
    }

    @Override
    public OrderDTO processReturn(Long orderId) {
        logger.info("Processing return for order: {}", orderId);

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(
                String.format(ApiConstants.ORDER_NOT_FOUND, orderId)));

        // Validate order can be returned
        if (order.getStatus() == OrderStatus.PENDING || 
            order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderException("Order cannot be returned in current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.RETURNED);
        Order returnedOrder = orderRepository.save(order);
        kafkaEventPublisher.publishOrderCreatedEvent(returnedOrder);
        logger.info("Order {} marked as returned", orderId);

        return mapToDTO(returnedOrder);
    }

    /**
     * Map Order entity to OrderDTO.
     * 
     * @param order Order entity
     * @return OrderDTO
     */
    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        dto.setItems(order.getItems().stream()
            .map(this::mapItemToDTO)
            .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Map OrderItem entity to OrderItemDTO.
     * 
     * @param item OrderItem entity
     * @return OrderItemDTO
     */
    private OrderItemDTO mapItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setItemId(item.getItemId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
