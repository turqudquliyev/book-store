package az.ingress.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

import static lombok.AccessLevel.PRIVATE;
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CacheUtil {
    RedissonClient redissonClient;
    public <T> T getBucket(String cacheKey) {
        RBucket<T> bucket = redissonClient.getBucket(cacheKey);
        return bucket == null ? null : bucket.get();
    }
    public <T> void saveToCache(String key,
                                T value,
                                Long expireTime,
                                TemporalUnit temporalUnit) {
        var bucket = redissonClient.getBucket(key);
        bucket.set(value);
        bucket.expire(Duration.of(expireTime, temporalUnit));
    }
}