package com.example.userservice.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class SecurityConfig {

  @Bean
  public FilterRegistrationBean<BearerAuthFilter> bearerAuthFilter(AuthProperties properties) {
    FilterRegistrationBean<BearerAuthFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new BearerAuthFilter(properties.getApiToken()));
    registration.addUrlPatterns("/*");
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registration;
  }
}
