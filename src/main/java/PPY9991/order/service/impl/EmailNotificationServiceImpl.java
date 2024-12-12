package PPY9991.order.service.impl;

import PPY9991.order.service.NotificationService;
import PPY9991.order.model.Inventory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final String ADMIN_EMAIL = "admin@example.com";

    @Override
    public void sendStockAlert(Inventory inventory) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ADMIN_EMAIL);
        message.setSubject("库存预警通知");
        message.setText(String.format(
            "商品ID：%d的库存数量已低于预警阈值！\n当前库存：%d\n预警阈值：%d",
            inventory.getProductId(),
            inventory.getQuantity(),
            inventory.getAlertThreshold()
        ));
        
        mailSender.send(message);
    }
} 