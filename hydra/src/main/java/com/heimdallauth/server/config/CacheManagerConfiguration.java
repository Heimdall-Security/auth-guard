package com.heimdallauth.server.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class CacheManagerConfiguration {
    @Bean
    public CacheManager caffeineCacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("publicJsonWebKeysCache","authorizationServerCache");
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory).build();
    }

    @Primary
    @Bean
    public CacheManager compositeCacheManager(
            CacheManager caffeineCacheManager,
            CacheManager redisCacheManager
    ) {
        return new CompositeCacheManager(caffeineCacheManager, redisCacheManager);
    }
}
