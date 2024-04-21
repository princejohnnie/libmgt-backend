package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.dtos.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenService {

    private static final long EXPIRATION_TIME = 86400000;

    private static final String SIGNING_KEY = "SIGNING_KEY";

    public String generateToken(Long librarianId) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .setSubject(librarianId.toString())
                .compact();
    }

    public TokenDto validateToken(String token) {
        var body = Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();

        var librarianId = Long.parseLong(body.getSubject());
        var issuedAt = body.getIssuedAt();
        var expiration = body.getExpiration();

        if (expiration.before(new Date())) {
            return null; // Token is expired
        }

        var tokenDto = new TokenDto();
        tokenDto.setLibrarianId(librarianId);
        tokenDto.setIssuedAt(issuedAt);
        tokenDto.setExpiration(expiration);

        return tokenDto;
    }


}
