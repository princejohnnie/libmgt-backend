package com.johnny.libmgtbackend.caching;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public CacheManager booksCacheManager() {
        return new ConcurrentMapCacheManager("books");
    }

    @Bean
    public CacheManager patronCacheManager() {
        return new ConcurrentMapCacheManager("patrons");
    }

    @Bean
    public CacheManager librarianCacheManager() {
        return new ConcurrentMapCacheManager("librarians");
    }
}
