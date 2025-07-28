package com.backend.adapter.inbound.web.v1;

import com.backend.adapter.inbound.web.dto.OrderItemRequest;
import com.backend.adapter.inbound.web.dto.OrderRequest;
import com.backend.adapter.inbound.web.dto.ProductRequest;
import com.backend.core.domain.enums.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderItemWebControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String orderId;
    private String productId;

    @BeforeEach
    void setup() throws Exception {
        productId = createProduct("Produto X", BigDecimal.valueOf(100), ProductType.PRODUCT);
        orderId = createOrder(productId);
    }

    @Test
    @DisplayName("Deve criar um OrderItem com sucesso")
    void testCreateOrderItem() throws Exception {
        OrderItemRequest request = new OrderItemRequest(UUID.fromString(productId), 3);

        mockMvc.perform(post("/api/orders/{orderId}/items", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    @DisplayName("Deve buscar um OrderItem por ID")
    void testFindOrderItemById() throws Exception {
        String itemId = createOrderItem(orderId, productId, 2);

        mockMvc.perform(get("/api/orders/{orderId}/items/{itemId}", orderId, itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    private String createProduct(String name, BigDecimal price, ProductType type) throws Exception {
        ProductRequest request = new ProductRequest(name, price, type, true);

        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asText();
    }

    private String createOrder(String productId) throws Exception {
        OrderItemRequest item = new OrderItemRequest(UUID.fromString(productId), 1);
        OrderRequest request = new OrderRequest(BigDecimal.ZERO, List.of(item));

        String response = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asText();
    }

    private String createOrderItem(String orderId, String productId, int quantity) throws Exception {
        OrderItemRequest request = new OrderItemRequest(UUID.fromString(productId), quantity);

        String response = mockMvc.perform(post("/api/orders/{orderId}/items", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asText();
    }
}
