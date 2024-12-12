package PPY9991.order.service;

import PPY9991.order.model.Logistics;
import PPY9991.order.dto.LogisticsUpdateRequest;
import java.util.List;

public interface LogisticsService {
    // 创建物流记录
    Logistics createLogistics(String orderNo);
    
    // 更新物流状态
    Logistics updateLogistics(LogisticsUpdateRequest request);
    
    // 获取物流信息
    Logistics getLogistics(String orderNo);
    
    // 获取物流轨迹
    List<LogisticsTrack> getLogisticsTrack(String orderNo);
    
    // 同步第三方物流信息
    void syncLogisticsInfo(String trackingNo);
} 