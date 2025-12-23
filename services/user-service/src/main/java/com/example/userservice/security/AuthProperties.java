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
  /**
   * Secret key for signing/verifying JWT (HS256). Minimum 32 chars recommended.
   */
  private String jwtSecret = "dev-jwt-secret-change-me-please-32-chars";

  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(String apiToken) {
    this.apiToken = apiToken;
  }

  public String getJwtSecret() {
    return jwtSecret;
  }

  public void setJwtSecret(String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }
}
