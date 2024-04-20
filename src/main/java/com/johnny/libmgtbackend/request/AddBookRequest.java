package com.johnny.libmgtbackend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddBookRequest {
    @NotBlank
    public String title;
    @NotBlank
    public String author;
    @NotBlank
    public String isbn;
    @NotBlank
    public String publicationYear;
}
