package com.bcal.o2o.identity.client;

import com.bcal.o2o.identity.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
    name = "master-data-service",
    url = "${master-data-service.url}"
)
public interface MasterDataClient {

  @GetMapping("/internal/users")
  List<UserDto> getAllUsers();

  @GetMapping("/internal/users/{id}")
  UserDto getUserById(@PathVariable Long id);

  @GetMapping("/internal/users/by-email")
  UserDto getUserByEmail(@RequestParam String email);

  @PostMapping("/internal/users")
  ResponseEntity<?> createUser(@RequestBody UserDto userDto);
}
