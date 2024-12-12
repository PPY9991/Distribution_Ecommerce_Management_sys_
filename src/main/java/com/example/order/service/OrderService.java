package com.example.order.service;

import com.example.order.model.Order;
import com.example.order.dto.OrderCreateRequest;

public interface OrderService {
    Order createOrder(OrderCreateRequest request);
    Order getOrder(Long orderId);
    Order updateOrderStatus(Long orderId, OrderStatus status);
    void cancelOrder(Long orderId);
} 