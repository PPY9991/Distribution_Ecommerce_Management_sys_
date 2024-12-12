package PPY9991.order.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long productId;
    
    private Integer quantity;
    
    private Integer lockQuantity;
    
    private Integer alertThreshold;
    
    @Version
    private Integer version;
    
    private LocalDateTime updateTime;
    
    // 获取可用库存数量
    public Integer getAvailableQuantity() {
        return quantity - lockQuantity;
    }
    
    // 判断是否需要库存预警
    public boolean needAlert() {
        return quantity <= alertThreshold;
    }
} 