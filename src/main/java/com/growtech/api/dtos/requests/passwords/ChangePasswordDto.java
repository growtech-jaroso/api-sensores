package com.growtech.api.dtos.requests.passwords;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.growtech.api.annotations.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches(password = "newPassword", message = "The new password and the confirmation password do not match")
public record ChangePasswordDto(
  @JsonProperty("old_password")
  @NotBlank(message = "The old password is required")
  String oldPassword,
  @JsonProperty("new_password")
  @NotBlank(message = "The new password is required")
  @Size(min = 8, message = "The new password must have at least 8 characters")
  String newPassword,
  @JsonProperty("confirm_password")
  @NotBlank(message = "The new password confirmation is required")
  String confirmPassword
) implements PasswordsInterface {

  @Override
  public String getPassword() {
    return newPassword();
  }

  @Override
  public String getConfirmPassword() {
    return confirmPassword();
  }
}
