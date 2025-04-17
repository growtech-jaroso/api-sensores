package com.proyecto.apisensores.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.apisensores.dtos.UserRegisterDto;
import com.proyecto.apisensores.enums.UserRole;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends Model implements UserDetails {

  @Indexed(unique = true)
  private String username;

  private String password;

  @Indexed(unique = true)
  private String email;

  private List<UserRole> roles = List.of(UserRole.USER);

  public User(UserRegisterDto dto) {
    super();
    this.username = dto.username();
    this.password = dto.password();
    this.email = dto.email();
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

  /**
   * Check if the user is an admin
   * @return true if the user is an admin, false otherwise
   */
  public Boolean isAdmin() {
    return this.roles.contains(UserRole.ADMIN);
  }
}
