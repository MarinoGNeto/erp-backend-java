package com.backend.adapter.inbound.web.v1;

import com.backend.adapter.inbound.web.dto.OrderItemRequest;
import com.backend.adapter.inbound.web.dto.OrderRequest;
import com.backend.adapter.inbound.web.dto.ProductRequest;
import com.backend.core.domain.enums.ProductType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class OrderWebControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create an Order successfully")
    void shouldCreateOrderSuccessfully() throws Exception {
        String productId = createProduct("Tênis", BigDecimal.valueOf(250), ProductType.PRODUCT);
        OrderItemRequest item = new OrderItemRequest(UUID.fromString(productId), 2);
        OrderRequest request = new OrderRequest(BigDecimal.valueOf(5), List.of(item));

        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.discountPercentage").value(5));
    }

    @Test
    @DisplayName("Should return 404 when creating order with invalid product")
    void shouldFailToCreateOrderWithInvalidProduct() throws Exception {
        OrderItemRequest item = new OrderItemRequest(UUID.randomUUID(), 2);
        OrderRequest request = new OrderRequest(BigDecimal.TEN, List.of(item));

        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Product not found")));
    }

    @Test
    @DisplayName("Should retrieve an existing Order by ID")
    void shouldFindOrderById() throws Exception {
        String productId = createProduct("Mouse Gamer", BigDecimal.valueOf(180), ProductType.PRODUCT);
        String orderId = createOrder(productId);

        mockMvc.perform(get("/api/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId));
    }

    @Test
    @DisplayName("Should return 404 when retrieving non-existing Order")
    void shouldReturnNotFoundForInvalidOrderId() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/api/orders/{id}", fakeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Order not found")));
    }

    @Test
    @DisplayName("Should update an Order successfully")
    void shouldUpdateOrderSuccessfully() throws Exception {
        String productId = createProduct("Notebook", BigDecimal.valueOf(180), ProductType.PRODUCT);
        String orderId = createOrder(productId);

        OrderItemRequest newItem = new OrderItemRequest(UUID.fromString(productId), 3);
        OrderRequest updateRequest = new OrderRequest(BigDecimal.valueOf(15), List.of(newItem));

        mockMvc.perform(put("/api/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discountPercentage").value(15));
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
        OrderRequest request = new OrderRequest(BigDecimal.TEN, List.of(item));
        String response = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asText();
    }
}
