package com.bcal.o2o.masterdata.user;

public class UserDto {

  private Long id;
  private String name;
  private String email;
  private String passwordHash;
  private String organization;

  public UserDto() {
  }

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }
}
