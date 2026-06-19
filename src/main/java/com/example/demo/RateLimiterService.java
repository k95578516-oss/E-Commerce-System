package com.example.demo;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiterService {

    private final Bucket bucket;

    public RateLimiterService() {

        Bandwidth limit =
                Bandwidth.simple(
                        100,
                        Duration.ofMinutes(1)
                );

        bucket =
                Bucket.builder()
                        .addLimit(limit)
                        .build();
    }

    public boolean allowRequest() {
        return bucket.tryConsume(1);
    }
}