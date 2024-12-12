package PPY9991.order.dto;

import lombok.Data;
import PPY9991.order.model.LogisticsStatus;

@Data
public class LogisticsUpdateRequest {
    private String orderNo;
    private String trackingNo;
    private String carrierName;
    private LogisticsStatus status;
    private String currentLocation;
    private String description;
} 