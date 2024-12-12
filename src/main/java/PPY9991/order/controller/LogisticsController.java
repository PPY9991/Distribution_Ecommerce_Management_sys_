package PPY9991.order.controller;

import PPY9991.order.service.LogisticsService;
import PPY9991.order.model.Logistics;
import PPY9991.order.model.LogisticsTrack;
import PPY9991.order.dto.LogisticsUpdateRequest;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    private final LogisticsService logisticsService;

    @GetMapping("/{orderNo}")
    public Logistics getLogistics(@PathVariable String orderNo) {
        return logisticsService.getLogistics(orderNo);
    }

    @GetMapping("/{orderNo}/track")
    public List<LogisticsTrack> getLogisticsTrack(@PathVariable String orderNo) {
        return logisticsService.getLogisticsTrack(orderNo);
    }

    @PostMapping("/update")
    public Logistics updateLogistics(@RequestBody LogisticsUpdateRequest request) {
        return logisticsService.updateLogistics(request);
    }

    @PostMapping("/sync/{trackingNo}")
    public void syncLogisticsInfo(@PathVariable String trackingNo) {
        logisticsService.syncLogisticsInfo(trackingNo);
    }
} 