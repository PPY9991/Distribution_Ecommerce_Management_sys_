package PPY9991.order.service;

import PPY9991.order.model.Order;
import PPY9991.order.dto.OrderCreateRequest;
import PPY9991.order.model.OrderStatus;

public interface OrderService {
    Order createOrder(OrderCreateRequest request);
    Order getOrder(Long orderId);
    Order updateOrderStatus(Long orderId, OrderStatus status);
    void cancelOrder(Long orderId);
} 