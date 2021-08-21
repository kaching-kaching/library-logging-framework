package com.kaching.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaching.logging.configuration.LoggingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfigurationTest implements WebMvcConfigurer {
  @Autowired private LoggingConfiguration config;

  @Bean
  LoggingInterceptor getLoggerInterceptor() {
    return new LoggingInterceptor(config, objectMapper());
  }

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(getLoggerInterceptor()).addPathPatterns("/**");
  }
}
