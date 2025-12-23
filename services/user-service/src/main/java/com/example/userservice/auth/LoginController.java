package com.example.userservice.auth;

import com.example.userservice.security.AuthProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  private final AuthProperties authProperties;

  public LoginController(AuthProperties authProperties) {
    this.authProperties = authProperties;
  }

  @PostMapping("/login")
  public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
    // Demo-only validation; in real apps, verify hashed passwords from a datastore.
    if ("admin".equals(request.username()) && "password".equals(request.password())) {
      return ResponseEntity.ok(new TokenResponse(authProperties.getApiToken(), "bearer"));
    }
    return ResponseEntity.status(401).build();
  }

  public record LoginRequest(String username, String password) {}

  public record TokenResponse(String token, String type) {}
}
