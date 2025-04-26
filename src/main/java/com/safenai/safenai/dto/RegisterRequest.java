package com.safenai.safenai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Must be a well-formed email address")
    private String email;

    @NotBlank(message = "Full name is required")
    private String Name;

    @NotBlank(message = "Password is required")
    private String password;

    
}