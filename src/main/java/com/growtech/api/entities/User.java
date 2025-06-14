package com.growtech.api.entities;

import com.growtech.api.dtos.requests.passwords.UserEditDto;
import com.growtech.api.dtos.responses.AuthInfo;
import com.growtech.api.dtos.responses.UserInfo;
import com.growtech.api.dtos.requests.passwords.UserRegisterDto;
import com.growtech.api.enums.UserRole;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@Builder
public class User extends Model implements UserDetails {

  @Indexed(unique = true)
  private String username;

  private String password;

  @Indexed(unique = true)
  private String email;

  @Indexed
  private UserRole role;

  public User() {
    super();
    this.role = UserRole.USER;
  }

  public User(String username, String password, String email, UserRole role) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = UserRole.USER;
  }

  public User(UserRegisterDto dto, String encryptedPassword) {
    super();
    this.username = dto.username();
    this.password = encryptedPassword;
    this.email = dto.email();
    this.role = UserRole.convertFromString(dto.role());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(this.role.name()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }

  /**
   * Check if the user can view anything
   * @return true if the user can view anything, false otherwise
   */
  public Boolean canViewAnything() {
    return this.role.equals(UserRole.SUPPORT) || this.role.equals(UserRole.ADMIN);
  }

  public Boolean isAdmin() {
    return this.role.equals(UserRole.ADMIN);
  }

  /**
   * Get the user info DTO from the user
   * @return the user info DTO
   */
  public UserInfo getUserInfoDto() {
    return new UserInfo(
      this.getId(),
      this.username,
      this.email,
      this.role
    );
  }

  public AuthInfo getAuthInfo(String token) {
    return new AuthInfo(this.id, token, this.username, this.email, this.role);
  }

  /**
   * Edit the user with the new information
   * @param userEditDto UserEditDto containing the new information
   */
  public void edit(UserEditDto userEditDto) {
    this.username = userEditDto.username();
    this.email = userEditDto.email();
    this.role = UserRole.convertFromString(userEditDto.role());
    this.updatedAt = LocalDateTime.now();
  }
}
