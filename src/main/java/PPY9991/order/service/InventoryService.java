package PPY9991.order.service;

import PPY9991.order.dto.OrderItemRequest;
import java.util.List;

public interface InventoryService {
    // 检查并锁定库存
    boolean checkAndLockStock(List<OrderItemRequest> items);
    
    // 释放库存
    void releaseStock(Long orderId);
    
    // 扣减库存（支付成功后调用）
    void deductStock(Long orderId);
    
    // 检查库存是否充足
    boolean checkStock(Long productId, Integer quantity);
    
    // 更新库存
    void updateStock(Long productId, Integer quantity);
    
    // 获取库存预警商品列表
    List<Inventory> getStockAlertList();
} 