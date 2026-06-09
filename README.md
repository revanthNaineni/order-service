# Order Service

Microservice for managing customer orders in the E-commerce platform.

## Overview

The Order Service is responsible for:
- Creating and managing customer orders
- Tracking order status (pending, processing, shipped, delivered, returned)
- Managing order items and quantities
- Publishing order events to Kafka for cross-service communication
- Processing returns and refunds

## Legacy Mapping

This microservice maps from the following legacy components:

| Legacy Component | Target Component |
|------------------|------------------|
| `CreateOrderCommand` | `OrderController.createOrder()` |
| `OrderDelegate` | `OrderService` |
| `OrderDAO` | `OrderRepository` |
| `OrderVO` | `OrderDTO` |
| `OrderItemVO` | `OrderItemDTO` |

## Architecture

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL (AWS RDS)
- **Messaging**: Apache Kafka (AWS MSK)
- **Security**: OAuth2/JWT via AWS Cognito
- **API Documentation**: OpenAPI 3.0 (Swagger)

## Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- PostgreSQL 14 or higher
- Apache Kafka 3.x
- Docker (optional, for containerization)

## Getting Started

### 1. Clone the repository
