package com.example.order.service.impl;

import com.example.order.service.OrderService;
import com.example.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    
    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request) {
        // 检查库存
        boolean hasStock = inventoryService.checkAndLockStock(request.getItems());
        if (!hasStock) {
            throw new BusinessException("库存不足");
        }
        
        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(request.getUserId());
        order.setTotalAmount(calculateTotalAmount(request.getItems()));
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreateTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new BusinessException("订单不存在"));
    }
    
    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        order.setUpdateTime(LocalDateTime.now());
        return orderRepository.save(order);
    }
    
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("只能取消待支付订单");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdateTime(LocalDateTime.now());
        orderRepository.save(order);
        // 释放库存
        inventoryService.releaseStock(orderId);
    }
} 