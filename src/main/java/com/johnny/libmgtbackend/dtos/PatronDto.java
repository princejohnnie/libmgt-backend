package com.johnny.libmgtbackend.dtos;

import com.johnny.libmgtbackend.models.Patron;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "items")
public class PatronDto {
    public Long id;
    public String name;
    public String contact;

    public PatronDto(Patron patron) {
        this.id = patron.getId();
        this.name = patron.getName();
        this.contact = patron.getContact();
    }
}
