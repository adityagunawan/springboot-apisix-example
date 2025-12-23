package com.example.userservice.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

  @GetMapping("/profile")
  public ResponseEntity<UserProfile> profile(HttpServletRequest request) {
    Object claimsObj = request.getAttribute("user");
    if (!(claimsObj instanceof Claims claims)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserProfile profile = new UserProfile(
        claims.getSubject(),
        claims.get("email", String.class),
        claims.get("fullName", String.class),
        claims.get("organization", String.class)
    );
    return ResponseEntity.ok(profile);
  }
}
