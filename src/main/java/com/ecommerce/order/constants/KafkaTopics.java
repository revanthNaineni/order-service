package com.ecommerce.order.constants;

/**
 * Kafka topic constants for event-driven communication.
 */
public final class KafkaTopics {
    
    private KafkaTopics() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String ORDER_EVENTS = "order-events";
    public static final String CUSTOMER_EVENTS = "customer-events";
}
