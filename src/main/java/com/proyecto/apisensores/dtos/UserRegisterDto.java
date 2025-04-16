package com.proyecto.apisensores.dtos;

import com.proyecto.apisensores.annotations.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldMatch(first = "password", second = "confirmPassword", message = "Passwords do not match")
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
  @Size(min = 8, message = "Password confirmation must be at least 8 characters")
  String confirmPassword
) { }
