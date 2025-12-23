package com.example.userservice.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security")
public class AuthProperties {
  /**
   * Bearer token that clients must send in Authorization header.
   */
  private String apiToken = "dev-secret-token";

  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(String apiToken) {
    this.apiToken = apiToken;
  }
}
