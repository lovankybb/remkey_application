package com.washinggod.remkey.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.washinggod.remkey.configuration.properties.MomoProperties;
import com.washinggod.remkey.configuration.properties.StaticLocation;
import com.washinggod.remkey.configuration.properties.VnpayProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GlobalConfiguration {

  @Bean
  MomoProperties momoProperties() {
    return new MomoProperties();
  }

  @Bean
  VnpayProperties vnpayProperties() {
    return new VnpayProperties();
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  StaticLocation staticLocation() {
    return new StaticLocation();
  }

  @Bean
  ObjectMapper objectMapper() {

    JavaTimeModule javaTimeModule = new JavaTimeModule();
    //        String to LocalDateTime
    javaTimeModule.addSerializer(
        LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    //        LocalDateTime to String
    javaTimeModule.addDeserializer(
        LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(javaTimeModule);
    return objectMapper;
  }
}
