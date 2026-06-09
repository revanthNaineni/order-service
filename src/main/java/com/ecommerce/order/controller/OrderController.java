package com.ecommerce.order.controller;

import com.ecommerce.order.constants.ApiConstants;
import com.ecommerce.order.model.dto.CreateOrderRequest;
import com.ecommerce.order.model.dto.CreateOrderResponse;
import com.ecommerce.order.model.dto.OrderDTO;
import com.ecommerce.order.model.dto.UpdateOrderStatusRequest;
import com.ecommerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Order Service.
 * Maps from legacy CreateOrderCommand and OrderDelegate.
 * 
 * Legacy Mapping:
 * - CreateOrderCommand.execute() -> createOrder()
 * - OrderDelegate.createOrder() -> createOrder()
 * - OrderDelegate.getOrderStatus() -> getOrderById()
 */
@RestController
@RequestMapping(ApiConstants.API_BASE_PATH)
@Tag(name = "Order Management", description = "APIs for managing customer orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order.
     * Maps from: CreateOrderCommand.execute() and OrderDelegate.createOrder(orderVO)
     * 
     * @param request Order creation request
     * @return Created order response
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Create new order", description = "Create a new customer order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader(value = ApiConstants.HEADER_CORRELATION_ID, required = false) String correlationId) {
        
        logger.info("Creating order for customer ID: {}, correlationId: {}", 
            request.getCustomerId(), correlationId);
        
        CreateOrderResponse response = orderService.createOrder(request);
        
        logger.info("Order created successfully with ID: {}", response.getOrderId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get order by ID.
     * Maps from: OrderDelegate.getOrderStatus(orderId)
     * 
     * @param id Order ID
     * @return Order details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Get order by ID", description = "Retrieve order details by order ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        logger.info("Retrieving order with ID: {}", id);
        
        OrderDTO order = orderService.getOrderById(id);
        
        return ResponseEntity.ok(order);
    }

    /**
     * Update order status.
     * Maps from: OrderDelegate.updateOrderStatus(orderId, status)
     * 
     * @param id Order ID
     * @param request Status update request
     * @return Updated order
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        
        logger.info("Updating order status for ID: {} to {}", id, request.getStatus());
        
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
        
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Process order return.
     * Maps from: OrderDelegate.processReturn(orderId)
     * 
     * @param id Order ID
     * @return Updated order with RETURNED status
     */
    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @Operation(summary = "Process order return", description = "Process a return for an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Return processed"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Order cannot be returned")
    })
    public ResponseEntity<OrderDTO> processReturn(@PathVariable Long id) {
        logger.info("Processing return for order ID: {}", id);
        
        OrderDTO returnedOrder = orderService.processReturn(id);
        
        return ResponseEntity.ok(returnedOrder);
    }
}
