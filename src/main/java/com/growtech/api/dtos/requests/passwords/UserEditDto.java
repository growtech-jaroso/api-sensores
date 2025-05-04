package com.growtech.api.dtos.requests.passwords;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.annotations.IsUserRole;
import com.growtech.api.annotations.PasswordMatches;
import jakarta.validation.constraints.*;

@PasswordMatches
public record UserEditDto(
  @NotBlank(message = "Username is required")
  @Size(min = 3, message = "Username must be at least 3 characters")
  String username,
  @NotBlank(message = "Email is required")
  @Email(message = "Email is invalid")
  String email,
  @NotNull(message = "Password is required")
  @Pattern(regexp = "^$|.{8,}", message = "Password must be empty or at least 8 characters")
  String password,
  @JsonProperty("confirm_password")
  @NotNull(message = "Confirm password is required")
  String confirmPassword,
  @NotBlank(message = "Role is required")
  @IsUserRole
  String role
) implements PasswordsInterface {
  @Override
  public String getPassword() {
    return this.password();
  }

  @Override
  public String getConfirmPassword() {
    return this.confirmPassword();
  }
}
