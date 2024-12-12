package PPY9991.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderCreateRequest {
    private Long userId;
    private List<OrderItemRequest> items;
    private String paymentMethod; // ALIPAY, WECHAT
}

@Data
public static class OrderItemRequest {
    private Long productId;
    private Integer quantity;
} 