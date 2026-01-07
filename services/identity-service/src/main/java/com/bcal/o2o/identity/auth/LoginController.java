package com.bcal.o2o.identity.auth;

import com.bcal.o2o.identity.client.MasterDataClient;
import com.bcal.o2o.identity.security.AuthProperties;
import com.bcal.o2o.identity.user.UserDto;
import feign.FeignException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {

  private final AuthProperties authProperties;
  private final MasterDataClient masterDataClient;
  private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder;
  private static final Duration TOKEN_TTL = Duration.ofHours(1);

  public LoginController(AuthProperties authProperties, MasterDataClient masterDataClient) {
    this.authProperties = authProperties;
    this.masterDataClient = masterDataClient;
    this.passwordEncoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
  }

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
      // Fetch user from master-data-service
      UserDto userDto = masterDataClient.getUserByEmail(request.username());

      if (userDto == null) {
        return ResponseEntity.status(401).body(
            com.bcal.o2o.identity.common.StandardResponse.error(
                401, "Invalid credentials", null));
      }

      // Verify password using BCrypt
      if (request.password() == null || request.password().isEmpty()) {
        return ResponseEntity.status(401).body(
            com.bcal.o2o.identity.common.StandardResponse.error(
                401, "Invalid credentials", null));
      }

      if (userDto.passwordHash() == null || !passwordEncoder.matches(request.password(), userDto.passwordHash())) {
        log.warn("Invalid password for user: {}", request.username());
        return ResponseEntity.status(401).body(
            com.bcal.o2o.identity.common.StandardResponse.error(
                401, "Invalid credentials", null));
      }

      // Build user profile from fetched data
      UserProfile user = new UserProfile(
          userDto.email(),
          userDto.email(),
          userDto.name(),
          userDto.organization()
      );

      // Generate JWT token
      Instant now = Instant.now();
      Instant expiry = now.plus(TOKEN_TTL);
      String token = Jwts.builder()
          .setSubject(user.username())
          .setIssuedAt(Date.from(now))
          .setExpiration(Date.from(expiry))
          .claim("email", user.email())
          .claim("fullName", user.fullName())
          .claim("organization", user.organization())
          .signWith(Keys.hmacShaKeyFor(authProperties.getJwtSecret().getBytes(StandardCharsets.UTF_8)),
              SignatureAlgorithm.HS256)
          .compact();

      TokenResponse tokenResponse = new TokenResponse(token, "bearer", TOKEN_TTL.toSeconds(), user);
      return ResponseEntity.ok(com.bcal.o2o.identity.common.StandardResponse.success(
          200, "login success", tokenResponse));

    } catch (FeignException.NotFound e) {
      log.warn("User not found: {}", request.username());
      return ResponseEntity.status(401).body(
          com.bcal.o2o.identity.common.StandardResponse.error(
              401, "Invalid credentials", null));
    } catch (FeignException e) {
      log.error("Error calling master data service: {}", e.getMessage(), e);
      return ResponseEntity.status(503).body(
          com.bcal.o2o.identity.common.StandardResponse.error(
              503, "Authentication service temporarily unavailable", null));
    }
  }

  public record LoginRequest(String username, String password) {}

  public record TokenResponse(String token, String type, long expiresInSeconds, UserProfile user) {}
}
