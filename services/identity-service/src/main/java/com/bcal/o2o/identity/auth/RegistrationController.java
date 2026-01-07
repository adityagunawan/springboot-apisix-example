package com.bcal.o2o.identity.auth;

import com.bcal.o2o.identity.client.MasterDataClient;
import com.bcal.o2o.identity.user.UserDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RegistrationController {

  private final MasterDataClient masterDataClient;
  private final BCryptPasswordEncoder passwordEncoder;

  public RegistrationController(MasterDataClient masterDataClient) {
    this.masterDataClient = masterDataClient;
    this.passwordEncoder = new BCryptPasswordEncoder();
  }

  @PostMapping("/auth/register")
  public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
    try {
      // Validate input
      if (request.email() == null || request.email().isEmpty()) {
        return ResponseEntity.badRequest().body(
            com.bcal.o2o.identity.common.StandardResponse.error(
                400, "Email is required", null));
      }

      if (request.password() == null || request.password().length() < 6) {
        return ResponseEntity.badRequest().body(
            com.bcal.o2o.identity.common.StandardResponse.error(
                400, "Password must be at least 6 characters", null));
      }

      if (request.name() == null || request.name().isEmpty()) {
        return ResponseEntity.badRequest().body(
            com.bcal.o2o.identity.common.StandardResponse.error(
                400, "Name is required", null));
      }

      // Hash password using BCrypt
      String hashedPassword = passwordEncoder.encode(request.password());

      // Create UserDto
      UserDto userDto = new UserDto(null, request.name, request.email, request.organization(), hashedPassword);

      // Call master-data-service to create user
      ResponseEntity<?> response = masterDataClient.createUser(userDto);

      if (response.getStatusCode().is2xxSuccessful()) {
        log.info("User registered successfully: {}", request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            com.bcal.o2o.identity.common.StandardResponse.success(
                201, "User registered successfully", response.getBody()));
      } else {
        return response;
      }

    } catch (FeignException.Conflict e) {
      log.warn("User already exists: {}", request.email());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(
          com.bcal.o2o.identity.common.StandardResponse.error(
              409, "User with this email already exists", null));
    } catch (FeignException e) {
      log.error("Error calling master data service: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
          com.bcal.o2o.identity.common.StandardResponse.error(
              503, "Registration service temporarily unavailable", null));
    } catch (Exception e) {
      log.error("Unexpected error during registration", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
          com.bcal.o2o.identity.common.StandardResponse.error(
              500, "Internal server error", null));
    }
  }

  public record RegistrationRequest(
      String name,
      String email,
      String password,
      String organization
  ) {}
}
