package com.washinggod.remkey.configuration;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import java.time.Duration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfiguration {

  //    create redis client to connect application to Redis
  @Bean
  public RedisClient redisClient(RedisProperties redisProperties) {

    return RedisClient.create(
        RedisURI.builder()
            .withHost(redisProperties.getHost())
            .withPort(redisProperties.getPort())
            .build());
  }

  /*
   * ProxyManager
   * Mission1: Mapping Redis to find where the bucket of the current user
   * Mission2: Atomicity, proxyManager request Redis calculate before response result
   * Mission3: Expiration: Due on ExpirationStrategy to clean the expired buckets automatically
   *
   * */
  @Bean
  public ProxyManager<String> proxyManager(RedisClient redisClient) {

    StatefulRedisConnection<String, byte[]> connection =
        redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

    return LettuceBasedProxyManager.builderFor(connection)
        .withExpirationStrategy(
            ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofHours(1L)))
        .build();
  }
}
