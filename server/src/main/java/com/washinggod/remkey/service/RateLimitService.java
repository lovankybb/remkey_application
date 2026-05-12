package com.washinggod.remkey.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

  @Autowired private ProxyManager<String> proxyManager;

  public Bucket resolveBucket(String userId) {
    BucketConfiguration configuration =
        BucketConfiguration.builder()
            .addLimit(limit -> limit.capacity(100).refillGreedy(150, Duration.ofSeconds(10)))
            .build();

    return proxyManager.builder().build(userId, () -> configuration);
  }
}
