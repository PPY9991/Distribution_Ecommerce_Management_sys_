package PPY9991.order.service.impl;

import PPY9991.order.service.InventoryService;
import PPY9991.order.repository.InventoryRepository;
import PPY9991.order.repository.OrderItemRepository;
import PPY9991.order.model.Inventory;
import PPY9991.order.model.OrderItem;
import PPY9991.order.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public boolean checkAndLockStock(List<OrderItemRequest> items) {
        for (OrderItemRequest item : items) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                .orElseThrow(() -> new BusinessException("商品库存不存在"));
                
            if (inventory.getAvailableQuantity() < item.getQuantity()) {
                return false;
            }
            
            // 锁定库存
            inventory.setLockQuantity(inventory.getLockQuantity() + item.getQuantity());
            inventory.setUpdateTime(LocalDateTime.now());
            inventoryRepository.save(inventory);
            
            // 检查是否需要库存预警
            if (inventory.needAlert()) {
                notificationService.sendStockAlert(inventory);
            }
        }
        return true;
    }
    
    @Override
    @Transactional
    public void releaseStock(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : orderItems) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                .orElseThrow(() -> new BusinessException("商品库存不存在"));
                
            inventory.setLockQuantity(inventory.getLockQuantity() - item.getQuantity());
            inventory.setUpdateTime(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }
    }
    
    @Override
    @Transactional
    public void deductStock(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        for (OrderItem item : orderItems) {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                .orElseThrow(() -> new BusinessException("商品库存不存在"));
                
            inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
            inventory.setLockQuantity(inventory.getLockQuantity() - item.getQuantity());
            inventory.setUpdateTime(LocalDateTime.now());
            inventoryRepository.save(inventory);
            
            // 检查是否需要库存预警
            if (inventory.needAlert()) {
                notificationService.sendStockAlert(inventory);
            }
        }
    }
    
    @Override
    public boolean checkStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new BusinessException("商品库存不存在"));
        return inventory.getAvailableQuantity() >= quantity;
    }
    
    @Override
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
            .orElseThrow(() -> new BusinessException("商品库存不存在"));
            
        inventory.setQuantity(quantity);
        inventory.setUpdateTime(LocalDateTime.now());
        inventoryRepository.save(inventory);
        
        // 检查是否需要库存预警
        if (inventory.needAlert()) {
            notificationService.sendStockAlert(inventory);
        }
    }
    
    @Override
    public List<Inventory> getStockAlertList() {
        return inventoryRepository.findByQuantityLessThanEqual("alertThreshold");
    }
} 