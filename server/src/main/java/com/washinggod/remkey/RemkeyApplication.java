package com.washinggod.remkey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RemkeyApplication {

  public static void main(String[] args) {
    SpringApplication.run(RemkeyApplication.class, args);
  }
}
