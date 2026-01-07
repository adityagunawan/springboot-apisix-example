package com.bcal.o2o.masterdata.dto;

import com.bcal.o2o.masterdata.entities.UserEntity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {

  private Long id;
  private String name;
  private String email;
  private String passwordHash;
  private String organization;

  public UserDto(Long id, String name, String email, String passwordHash, String organization) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
    this.organization = organization;
  }

  public static UserDto fromEntity(UserEntity entity) {
    return new UserDto(
      entity.getId(),
      entity.getName(),
      entity.getEmail(),
      entity.getPasswordHash(),
      entity.getOrganization()
    );
  }

}
