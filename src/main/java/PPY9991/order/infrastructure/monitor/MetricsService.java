package PPY9991.order.infrastructure.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MeterRegistry registry;
    
    private Counter orderCounter;
    private Counter paymentCounter;
    
    public void init() {
        orderCounter = Counter.builder("order.created")
            .description("Number of orders created")
            .register(registry);
            
        paymentCounter = Counter.builder("payment.success")
            .description("Number of successful payments")
            .register(registry);
    }
    
    public void incrementOrderCount() {
        orderCounter.increment();
    }
    
    public void incrementPaymentCount() {
        paymentCounter.increment();
    }
} 