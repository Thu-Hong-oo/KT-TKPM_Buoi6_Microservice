package com.example.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long customerId;
    private List<OrderItemRequest> items;
}

@Data
class OrderItemRequest {
    private Long productId;
    private Integer quantity;
} 