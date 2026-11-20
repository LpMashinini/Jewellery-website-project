package com.Backend.Jewellery.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String ipAddress){
        return buckets.computeIfAbsent(ipAddress, this::createBucket);
    }

    private Bucket createBucket(String ipAddress) {
        Bandwidth limit = Bandwidth.classic(
                5, Refill.greedy(5, Duration.ofMinutes(1))
        );

        return Bucket4j.builder().addLimit(limit).build();
    }

}
