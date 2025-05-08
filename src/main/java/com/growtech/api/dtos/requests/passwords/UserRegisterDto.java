package com.growtech.api.dtos.requests.passwords;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.annotations.IsUserRole;
import com.growtech.api.annotations.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches()
public record UserRegisterDto(
  @NotBlank(message = "Username is required")
  @Size(min = 3, message = "Username must be at least 3 characters")
  String username,
  @NotBlank(message = "Email is required")
  @Email(message = "Email is invalid")
  String email,
  @NotBlank(message = "Password is required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  String password,
  @NotBlank(message = "Password confirmation is required")
  @JsonProperty("confirm_password")
  String confirmPassword,
  @IsUserRole
  String role
) implements PasswordsInterface {
  @Override
  public String getPassword() {
    return password();
  }

  @Override
  public String getConfirmPassword() {
    return confirmPassword();
  }
}
