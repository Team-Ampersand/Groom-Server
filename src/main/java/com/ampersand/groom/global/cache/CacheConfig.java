package com.ampersand.groom.global.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    @Value("${spring.cache.expiry-time}")
    private int expiryTime;

    @Value("${spring.cache.maximum-size}")
    private int maximumSize;

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(expiryTime, TimeUnit.MINUTES)
                .maximumSize(maximumSize);
    }

    @Bean
    public CaffeineCacheManager cacheManager(Caffeine<Object, Object> caffeineConfig) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(caffeineConfig);
        return caffeineCacheManager;
    }
}