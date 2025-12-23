package com.example.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Simple bearer token filter to protect API endpoints.
 */
public class BearerAuthFilter extends OncePerRequestFilter {

  private final String expectedToken;

  public BearerAuthFilter(String expectedToken) {
    this.expectedToken = expectedToken;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      unauthorized(response);
      return;
    }

    String token = authHeader.substring("Bearer ".length());
    if (!expectedToken.equals(token)) {
      unauthorized(response);
      return;
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    // Allow login, health, and preflight requests without auth
    String path = request.getRequestURI();
    return "OPTIONS".equalsIgnoreCase(request.getMethod())
        || path.startsWith("/login")
        || path.startsWith("/actuator");
  }

  private void unauthorized(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().write("Unauthorized");
  }
}
