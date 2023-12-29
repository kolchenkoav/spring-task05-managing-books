package com.example.books.configuration;

import com.example.books.configuration.properties.AppCacheProperties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties(AppCacheProperties.class)
public class CacheConfiguration {
    @Bean
    @ConditionalOnProperty(prefix = "app.redis", name = "enable", havingValue = "true")
    @ConditionalOnExpression("'${app.cache.cacheType}'.equals('REDIS')")
    public CacheManager redisCacheManager(AppCacheProperties appCacheProperties,
                                          LettuceConnectionFactory lettuceConnectionFactory) {
        log.info("=> CacheManager redisCacheManager ");
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        appCacheProperties.getCacheNames().forEach(cacheName ->
                redisCacheConfigurationMap.put(cacheName,
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                                appCacheProperties.getCaches().get(cacheName).getExpiry()
                        )));

        return RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }
}
