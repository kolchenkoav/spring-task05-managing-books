package com.example.books.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.redis", name = "enable", havingValue = "true")
public class RedisConfig {
    private final RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();

        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}
