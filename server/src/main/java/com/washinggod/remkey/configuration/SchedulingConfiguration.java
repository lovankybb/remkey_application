package com.washinggod.remkey.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulingConfiguration {

  @Bean
  public TaskScheduler taskScheduler() {

    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(5); // default allow 5 tasks to run concurrently
    scheduler.setThreadNamePrefix("Remkey-Scheduled-Task-");
    scheduler.initialize();
    return scheduler;
  }
}
