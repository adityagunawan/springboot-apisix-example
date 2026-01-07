package com.bcal.o2o.identity.user;

public record UserDto(Long id, String name, String email, String organization, String passwordHash) {
}
