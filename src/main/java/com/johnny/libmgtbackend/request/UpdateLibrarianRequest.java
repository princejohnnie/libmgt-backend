package com.johnny.libmgtbackend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UpdateLibrarianRequest {

    @Email
    public String email;
    public String name;
    @Size(min = 5, max = 25)
    public String password;
}
