package PPY9991.order.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "logistics_track")
public class LogisticsTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNo;
    
    private String trackingNo;
    
    private String location;
    
    private String description;
    
    private LocalDateTime trackTime;
    
    private LocalDateTime createTime;
} 