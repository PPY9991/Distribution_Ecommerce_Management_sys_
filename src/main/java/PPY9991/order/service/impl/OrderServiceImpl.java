package PPY9991.order.service.impl;

import PPY9991.order.service.OrderService;
import PPY9991.order.repository.OrderRepository;
import PPY9991.order.model.Order;
import PPY9991.order.model.OrderStatus;
import PPY9991.order.dto.OrderCreateRequest;
import PPY9991.order.exception.BusinessException;
import PPY9991.order.service.InventoryService;
import PPY9991.order.service.PaymentService;
import PPY9991.order.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ProductService productService;
    
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
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreateTime(LocalDateTime.now());
        
        // 创建订单项
        List<OrderItem> orderItems = request.getItems().stream()
            .map(item -> createOrderItem(item, order))
            .collect(Collectors.toList());
        order.setItems(orderItems);
        
        // 计算总金额
        BigDecimal totalAmount = orderItems.stream()
            .map(OrderItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);
        
        // 保存订单
        order = orderRepository.save(order);
        
        // 创建支付
        String paymentNo = paymentService.createPayment(order);
        order.setPaymentNo(paymentNo);
        
        return orderRepository.save(order);
    }
    
    private OrderItem createOrderItem(OrderItemRequest itemRequest, Order order) {
        Product product = productService.getProduct(itemRequest.getProductId());
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setUnitPrice(product.getPrice());
        orderItem.setSubtotal(product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity())));
        return orderItem;
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
    
    @Override
    public Order getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
            .orElseThrow(() -> new BusinessException("订单不存在"));
    }
} 