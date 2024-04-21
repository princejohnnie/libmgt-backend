package com.johnny.libmgtbackend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginRequest {
    @Email
    public String email;
    @NotBlank
    public String password;
}
