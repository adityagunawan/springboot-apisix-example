package com.example.userservice.user;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final List<Map<String, Object>> USERS = List.of(
      Map.of("id", 1, "name", "Alice", "email", "alice@example.com"),
      Map.of("id", 2, "name", "Bob", "email", "bob@example.com")
  );

  @GetMapping
  public List<Map<String, Object>> listUsers() {
    return USERS;
  }

  @GetMapping("/{id}")
  public Map<String, Object> getUser(@PathVariable int id) {
    Optional<Map<String, Object>> user =
        USERS.stream().filter(u -> u.get("id").equals(id)).findFirst();
    return user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }
}
