package com.johnny.libmgtbackend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequest {

    public String title;
    public String author;
    public String isbn;
    public String publicationYear;
}
