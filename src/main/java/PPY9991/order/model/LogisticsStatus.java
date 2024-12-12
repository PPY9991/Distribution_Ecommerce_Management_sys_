package PPY9991.order.model;

public enum LogisticsStatus {
    PENDING("待发货"),
    SHIPPED("已发货"),
    IN_TRANSIT("运输中"),
    DELIVERED("已送达"),
    EXCEPTION("异常");
    
    private final String description;
    
    LogisticsStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 