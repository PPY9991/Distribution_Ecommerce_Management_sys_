package PPY9991.order.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "logistics")
public class Logistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNo;
    
    private String trackingNo;
    
    private String carrierName;
    
    @Enumerated(EnumType.STRING)
    private LogisticsStatus status;
    
    private String currentLocation;
    
    private String description;
    
    private LocalDateTime updateTime;
    
    @Version
    private Integer version;
} 