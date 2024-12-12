package PPY9991.order.service.impl;

import PPY9991.order.service.PaymentService;
import PPY9991.order.model.Order;
import PPY9991.order.model.OrderStatus;
import com.alipay.api.AlipayClient;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlipayServiceImpl implements PaymentService {
    
    private final AlipayClient alipayClient;
    private final OrderService orderService;
    
    @Override
    public String createPayment(Order order) {
        // 调用支付宝SDK创建支付订单
        // 这里是示例代码，实际需要根据支付宝SDK文档实现
        try {
            AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
            request.setBizContent("{" +
                "\"out_trade_no\":\"" + order.getOrderNo() + "\"," +
                "\"total_amount\":\"" + order.getTotalAmount() + "\"," +
                "\"subject\":\"订单支付\"" +
                "}");
            AlipayTradeCreateResponse response = alipayClient.execute(request);
            if(response.isSuccess()) {
                return response.getTradeNo();
            }
        } catch (AlipayApiException e) {
            throw new BusinessException("创建支付订单失败");
        }
        return null;
    }
    
    @Override
    public void handlePaymentCallback(String orderNo, String paymentStatus) {
        Order order = orderService.getOrderByOrderNo(orderNo);
        if ("TRADE_SUCCESS".equals(paymentStatus)) {
            orderService.updateOrderStatus(order.getId(), OrderStatus.PAID);
        } else if ("TRADE_CLOSED".equals(paymentStatus)) {
            orderService.updateOrderStatus(order.getId(), OrderStatus.CANCELLED);
        }
    }
} 