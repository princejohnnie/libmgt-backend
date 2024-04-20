package com.johnny.libmgtbackend.dtos;

import com.johnny.libmgtbackend.models.Librarian;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "items")
public class LibrarianDto {
    public Long id;
    public String email;
    public String name;

    public LibrarianDto(Librarian librarian) {
        this.id = librarian.getId();
        this.email = librarian.getEmail();
        this.name = librarian.getName();
    }
}
