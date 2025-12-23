package com.example.userservice.user;

public record UserDto(Long id, String name, String email, String organization) {
  public static UserDto fromEntity(UserEntity entity) {
    return new UserDto(entity.getId(), entity.getName(), entity.getEmail(), entity.getOrganization());
  }
}
