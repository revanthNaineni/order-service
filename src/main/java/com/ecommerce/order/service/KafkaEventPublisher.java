package com.ecommerce.order.service;

import com.ecommerce.order.constants.KafkaTopics;
import com.ecommerce.order.model.entity.Order;
import com.ecommerce.order.model.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Service for publishing events to Kafka.
 * Implements event-driven architecture for cross-service communication.
 */
@Service
public class KafkaEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publish OrderCreatedEvent to Kafka.
     * Consumed by Order History Service to auto-create order history entry.
     * 
     * @param order Created order
     */
    public void publishOrderCreatedEvent(Order order) {
        try {
            OrderCreatedEvent event=new OrderCreatedEvent();
            event.setOrderId(order.getOrderId());
            event.setCustomerId(order.getCustomerId());
            event.setTotalAmount(order.getTotalAmount());
            event.setOrderStatus(order.getStatus().getDisplayName());
            event.setTimestamp(System.currentTimeMillis());
            event.getOrderDate(LocalDateTime.now());
//            Map<String, Object> event = new HashMap<>();
//            event.put("eventType", "OrderCreated");
//            event.put("orderId", order.getOrderId());
//            event.put("customerId", order.getCustomerId());
//            event.put("orderDate", order.getOrderDate().toString());
//            event.put("status", order.getStatus().name());
//            event.put("totalAmount", order.getTotalAmount());
//            event.put("timestamp", System.currentTimeMillis());

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(
                            KafkaTopics.ORDER_EVENTS,
                            order.getOrderId().toString(),
                            event
                    );

            //Handle success & failure asynchronously
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Failed to publish OrderCreatedEvent for order Id: {}",
                            order.getOrderId(), ex);
                } else {
                    logger.info("Published OrderCreatedEvent for order ID: {}, partition={}, offset={}",
                            order.getOrderId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                    logger.info(result.getRecordMetadata().topic());
                }
            });

            logger.info("Ousmane Dembele Published OrderCreatedEvent for order ID: {}", order.getOrderId());
        } catch (Exception e) {
            logger.error("Failed to publish OrderCreatedEvent for order ID: {}", 
                order.getOrderId(), e);
            // Don't throw exception - event publishing failure shouldn't fail order creation
        }
    }
}
