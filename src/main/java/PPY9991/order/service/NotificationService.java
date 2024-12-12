package PPY9991.order.service;

import PPY9991.order.model.Inventory;

public interface NotificationService {
    void sendStockAlert(Inventory inventory);
} 