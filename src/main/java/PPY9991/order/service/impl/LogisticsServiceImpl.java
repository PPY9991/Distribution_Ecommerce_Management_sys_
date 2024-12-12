package PPY9991.order.service.impl;

import PPY9991.order.service.LogisticsService;
import PPY9991.order.repository.LogisticsRepository;
import PPY9991.order.repository.LogisticsTrackRepository;
import PPY9991.order.model.Logistics;
import PPY9991.order.model.LogisticsTrack;
import PPY9991.order.model.LogisticsStatus;
import PPY9991.order.dto.LogisticsUpdateRequest;
import PPY9991.order.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository logisticsRepository;
    private final LogisticsTrackRepository logisticsTrackRepository;
    private final ThirdPartyLogisticsService thirdPartyLogisticsService;

    @Override
    @Transactional
    public Logistics createLogistics(String orderNo) {
        Logistics logistics = new Logistics();
        logistics.setOrderNo(orderNo);
        logistics.setStatus(LogisticsStatus.PENDING);
        logistics.setUpdateTime(LocalDateTime.now());
        return logisticsRepository.save(logistics);
    }

    @Override
    @Transactional
    public Logistics updateLogistics(LogisticsUpdateRequest request) {
        Logistics logistics = logisticsRepository.findByOrderNo(request.getOrderNo())
            .orElseThrow(() -> new BusinessException("物流信息不存在"));
        
        // 更新物流信息
        logistics.setTrackingNo(request.getTrackingNo());
        logistics.setCarrierName(request.getCarrierName());
        logistics.setStatus(request.getStatus());
        logistics.setCurrentLocation(request.getCurrentLocation());
        logistics.setDescription(request.getDescription());
        logistics.setUpdateTime(LocalDateTime.now());
        
        // 保存物流轨迹
        LogisticsTrack track = new LogisticsTrack();
        track.setOrderNo(request.getOrderNo());
        track.setTrackingNo(request.getTrackingNo());
        track.setLocation(request.getCurrentLocation());
        track.setDescription(request.getDescription());
        track.setTrackTime(LocalDateTime.now());
        track.setCreateTime(LocalDateTime.now());
        logisticsTrackRepository.save(track);
        
        return logisticsRepository.save(logistics);
    }

    @Override
    public Logistics getLogistics(String orderNo) {
        return logisticsRepository.findByOrderNo(orderNo)
            .orElseThrow(() -> new BusinessException("物流信息不存在"));
    }

    @Override
    public List<LogisticsTrack> getLogisticsTrack(String orderNo) {
        return logisticsTrackRepository.findByOrderNoOrderByTrackTimeDesc(orderNo);
    }

    @Override
    @Transactional
    public void syncLogisticsInfo(String trackingNo) {
        // 调用第三方物流API获取最新物流信息
        LogisticsInfo thirdPartyInfo = thirdPartyLogisticsService.queryLogisticsInfo(trackingNo);
        
        // 更新物流状态
        Logistics logistics = logisticsRepository.findByTrackingNo(trackingNo)
            .orElseThrow(() -> new BusinessException("物流信息不存在"));
        
        LogisticsUpdateRequest request = new LogisticsUpdateRequest();
        request.setOrderNo(logistics.getOrderNo());
        request.setTrackingNo(trackingNo);
        request.setStatus(convertThirdPartyStatus(thirdPartyInfo.getStatus()));
        request.setCurrentLocation(thirdPartyInfo.getLocation());
        request.setDescription(thirdPartyInfo.getDescription());
        
        updateLogistics(request);
    }
    
    private LogisticsStatus convertThirdPartyStatus(String thirdPartyStatus) {
        // 根据第三方物流状态���换为系统物流状态
        // 这里需要根据实际使用的物流服务商来实现转换逻辑
        return LogisticsStatus.IN_TRANSIT;
    }
} 