package com.johnny.libmgtbackend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreatePatronRequest {
    @NotBlank
    public String name;
    @NotBlank
    public String contact;
}
