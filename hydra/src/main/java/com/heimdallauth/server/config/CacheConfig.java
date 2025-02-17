package com.heimdallauth.server.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public Caffeine<Object, Object> caffeineCacheConfig(){
        return Caffeine.newBuilder().expireAfterWrite(Duration.of(30, ChronoUnit.MINUTES)).maximumSize(1000);
    }
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.of(15, ChronoUnit.HOURS)).disableCachingNullValues();
    }
}
