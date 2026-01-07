package com.bcal.o2o.masterdata.controllers;

import com.bcal.o2o.masterdata.entities.UserEntity;
import com.bcal.o2o.masterdata.dto.UserDto;
import com.bcal.o2o.masterdata.entities.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

  private final UserRepository repository;

  public InternalUserController(UserRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public List<UserDto> getAllUsers() {
    return repository.findAll()
      .stream()
      .map(UserDto::fromEntity)
      .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public UserDto getUserById(@PathVariable Long id) {
    return repository.findById(id)
      .map(UserDto::fromEntity)
      .orElse(null);
  }

  @GetMapping("/by-email")
  public UserDto getUserByEmail(@RequestParam String email) {
    return repository.findByEmail(email)
      .map(UserDto::fromEntity)
      .orElse(null);
  }

  @PostMapping
  public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
    // Check if user already exists
    if (repository.findByEmail(userDto.getEmail()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("User with email " + userDto.getEmail() + " already exists");
    }

    // Create new user entity
    UserEntity entity = new UserEntity();
    entity.setName(userDto.getName());
    entity.setEmail(userDto.getEmail());
    entity.setPasswordHash(userDto.getPasswordHash());
    entity.setOrganization(userDto.getOrganization());

    // Save to database
    UserEntity saved = repository.save(entity);

    // Return created user (without password hash)
    UserDto result = UserDto.fromEntity(saved);
    result.setPasswordHash(null); // Don't return password hash

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }
}
