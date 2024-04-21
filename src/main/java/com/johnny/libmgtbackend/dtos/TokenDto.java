package com.johnny.libmgtbackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenDto {
    private Long librarianId;
    private Date issuedAt;
    private Date expiration;
}
