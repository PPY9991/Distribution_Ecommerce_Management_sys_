package PPY9991.order.controller;

import PPY9991.order.service.PaymentService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment/callback")
@RequiredArgsConstructor
public class PaymentCallbackController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/alipay")
    public String handleAlipayCallback(@RequestParam String orderNo, 
                                     @RequestParam String tradeStatus) {
        paymentService.handlePaymentCallback(orderNo, tradeStatus);
        return "success";
    }
} 