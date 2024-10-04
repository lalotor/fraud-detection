package org.alwayslearning.frauddetection.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

  @Bean
  public CacheManager cacheManager(@Value("${appconfig.cache.enabled}") String isCacheEnabled) {
    if (isCacheEnabled.equalsIgnoreCase("false")) {
      log.debug("Caching not enabled");
      return new NoOpCacheManager();
    }

    log.debug("Caching enabled");
    return new ConcurrentMapCacheManager();
  }
}