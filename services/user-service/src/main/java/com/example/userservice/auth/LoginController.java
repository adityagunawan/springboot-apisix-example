package com.example.userservice.auth;

import com.example.common.response.StandardResponse;
import com.example.common.security.AuthProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private final AuthProperties authProperties;
  private static final Duration TOKEN_TTL = Duration.ofHours(1);

  public LoginController(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // Demo-only validation; in real apps, verify hashed passwords from a datastore.
    if ("admin".equals(request.username()) && "password".equals(request.password())) {
      UserProfile user = new UserProfile(
          request.username(),
          "admin@example.com",
          "Admin User",
          "Example Corp"
      );

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
      return ResponseEntity.ok(StandardResponse.success(200, "login success", tokenResponse));
    }
    return ResponseEntity.status(401).body(StandardResponse.error(401, "Invalid credentials", null));
  }

  public record LoginRequest(String username, String password) {}

  public record TokenResponse(String token, String type, long expiresInSeconds, UserProfile user) {}
}
