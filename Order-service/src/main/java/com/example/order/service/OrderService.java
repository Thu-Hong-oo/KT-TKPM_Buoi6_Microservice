package com.example.order.service;

import com.example.order.dto.OrderRequest;
import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${service.customer.url}")
    private String customerServiceUrl;

    @Value("${service.product.url}")
    private String productServiceUrl;

    public Order createOrder(OrderRequest orderRequest) {
        // Verify customer exists
        Boolean customerExists = webClientBuilder.build()
                .get()
                .uri(customerServiceUrl + "/api/customers/exists/" + orderRequest.getCustomerId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (!customerExists) {
            throw new RuntimeException("Customer not found");
        }

        Order order = new Order();
        order.setCustomerId(orderRequest.getCustomerId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            // Get product price from product service
            Double productPrice = webClientBuilder.build()
                    .get()
                    .uri(productServiceUrl + "/api/products/price/" + itemRequest.getProductId())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(productPrice);
            orderItem.setSubtotal(productPrice * itemRequest.getQuantity());

            orderItems.add(orderItem);
            totalAmount += orderItem.getSubtotal();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
} 