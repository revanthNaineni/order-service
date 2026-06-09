package com.ecommerce.order.controller;

import com.ecommerce.order.constants.OrderStatus;
import com.ecommerce.order.model.dto.*;
import com.ecommerce.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for OrderController.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JpaMetamodelMappingContext jpaMappingContext;

    private CreateOrderRequest createOrderRequest;
    private CreateOrderResponse createOrderResponse;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        // Setup test data
        OrderItemDTO item = new OrderItemDTO("ITEM123", 2, new BigDecimal("29.99"));
        
        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setCustomerId(123L);
        createOrderRequest.setItems(Arrays.asList(item));

        createOrderResponse = new CreateOrderResponse(
            1L,
            123L,
            OrderStatus.PENDING,
            new BigDecimal("59.98"),
            LocalDateTime.now()
        );

        orderDTO = new OrderDTO();
        orderDTO.setOrderId(1L);
        orderDTO.setCustomerId(123L);
        orderDTO.setStatus(OrderStatus.PENDING);
        orderDTO.setTotalAmount(new BigDecimal("59.98"));
        orderDTO.setOrderDate(LocalDateTime.now());
        orderDTO.setItems(Arrays.asList(item));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void testCreateOrder_Success() throws Exception {
        when(orderService.createOrder(any(CreateOrderRequest.class)))
            .thenReturn(createOrderResponse);

        mockMvc.perform(post("/api/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").value(1L))
            .andExpect(jsonPath("$.customerId").value(123L))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void testGetOrderById_Success() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        mockMvc.perform(get("/api/orders/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(1L))
            .andExpect(jsonPath("$.customerId").value(123L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateOrderStatus_Success() throws Exception {
        orderDTO.setStatus(OrderStatus.SHIPPED);
        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.SHIPPED)))
            .thenReturn(orderDTO);

        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(OrderStatus.SHIPPED);

        mockMvc.perform(put("/api/orders/1/status")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("SHIPPED"));
    }

    @Test
    void testCreateOrder_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/orders")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderRequest)))
            .andExpect(status().isUnauthorized());
    }
}
