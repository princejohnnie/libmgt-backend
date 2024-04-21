package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.auth.AuthenticationProvider;
import com.johnny.libmgtbackend.dtos.ErrorDto;
import com.johnny.libmgtbackend.dtos.LibrarianDto;
import com.johnny.libmgtbackend.request.RegisterRequest;
import com.johnny.libmgtbackend.request.LoginRequest;
import com.johnny.libmgtbackend.service.AuthenticationService;
import com.johnny.libmgtbackend.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationProvider authProvider;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/auth")
    ResponseEntity<?> authenticatedLibrarian() {
        try {
            var loggedInLibrarian = authProvider.getAuthenticatedLibrarian();
            return new ResponseEntity<>(new LibrarianDto(loggedInLibrarian), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            var newLibrarian = authService.register(request);
            return new ResponseEntity<>(tokenService.generateToken(newLibrarian.id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            var librarian = authService.login(request);
            if (BCrypt.checkpw(request.password, librarian.getPassword())) {
                return new ResponseEntity<>(tokenService.generateToken(librarian.getId()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorDto("Incorrect Credentials"), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
