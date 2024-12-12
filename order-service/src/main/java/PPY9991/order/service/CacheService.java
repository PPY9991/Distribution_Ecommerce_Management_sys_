package PPY9991.order.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final long DEFAULT_CACHE_TIME = 30 * 60; // 30分钟

    @Cacheable(value = "order", key = "#orderNo", unless = "#result == null")
    public Optional<Order> getOrder(String orderNo) {
        // 从数据库获取订单信息
        return orderRepository.findByOrderNo(orderNo);
    }

    @CacheEvict(value = "order", key = "#orderNo")
    public void clearOrderCache(String orderNo) {
        // 清除订单缓存
    }

    public void setWithExpire(String key, Object value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
} 