package PPY9991.order.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderNoGenerator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final AtomicInteger sequence = new AtomicInteger(0);
    
    public static String generate() {
        String timestamp = LocalDateTime.now().format(formatter);
        int seq = sequence.incrementAndGet() % 10000;
        return String.format("%s%04d", timestamp, seq);
    }
} 