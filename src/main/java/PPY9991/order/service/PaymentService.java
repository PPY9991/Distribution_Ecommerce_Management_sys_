package PPY9991.order.service;

import PPY9991.order.model.Order;

public interface PaymentService {
    String createPayment(Order order);
    void handlePaymentCallback(String orderNo, String paymentStatus);
} 