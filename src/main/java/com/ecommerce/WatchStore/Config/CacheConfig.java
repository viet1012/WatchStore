package com.ecommerce.WatchStore.Config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching // Bật sử dụng cache
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("myCache");
    }

}
