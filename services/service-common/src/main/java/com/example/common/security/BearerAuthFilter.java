package com.example.common.security;

import com.example.common.response.ErrorDetail;
import com.example.common.response.StandardResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT bearer filter to protect API endpoints.
 */
public class BearerAuthFilter extends OncePerRequestFilter {

  private final String jwtSecret;
  private final List<String> permitAll;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public BearerAuthFilter(String jwtSecret, List<String> permitAll) {
    this.jwtSecret = jwtSecret;
    this.permitAll = permitAll;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      unauthorized(request, response);
      return;
    }

    String token = authHeader.substring("Bearer ".length());

    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
          .build()
          .parseClaimsJws(token)
          .getBody();

      request.setAttribute("user", claims);
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      unauthorized(request, response, "Invalid or expired token");
    }
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }
    return permitAll.stream().filter(Objects::nonNull).anyMatch(path::startsWith);
  }

  private void unauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
    unauthorized(request, response, "Unauthorized");
  }

  private void unauthorized(
      HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    ErrorDetail errorDetail = new ErrorDetail("UNAUTHORIZED", message, Map.of("path", request.getRequestURI()));
    StandardResponse<Object> body = StandardResponse.error(HttpStatus.UNAUTHORIZED.value(), message, errorDetail);
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }
}
