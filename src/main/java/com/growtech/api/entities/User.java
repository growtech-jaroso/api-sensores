package com.growtech.api.entities;

import com.growtech.api.dtos.requests.UserRegisterDto;
import com.growtech.api.enums.UserRole;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
  private List<UserRole> roles;

  public User() {
    super();
    this.roles = List.of(UserRole.USER);
  }

  public User(String username, String password, String email, List<UserRole> roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.roles = roles;
  }

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.roles = List.of(UserRole.USER);
  }

  public User(UserRegisterDto dto) {
    super();
    this.username = dto.username();
    this.password = dto.password();
    this.email = dto.email();
    this.roles = List.of(UserRole.USER);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
      .map(role -> new SimpleGrantedAuthority(role.name()))
      .collect(Collectors.toList());
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
    return this.roles.contains(UserRole.SUPPORT) || this.roles.contains(UserRole.ADMIN);
  }

  public Boolean isAdmin() {
    return this.roles.contains(UserRole.ADMIN);
  }
}
