package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.dtos.LibrarianDto;
import com.johnny.libmgtbackend.exception.ModelNotFoundException;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.request.RegisterRequest;
import com.johnny.libmgtbackend.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private LibrarianRepository librarianRepository;

    public LibrarianDto register(RegisterRequest request) throws Exception {
        var existingLibrarian = librarianRepository.findByEmail(request.email);
        if (existingLibrarian != null) {
            throw new Exception("Librarian with email already exists");
        }
        var librarian = new Librarian(request.email, request.name, request.password);
        return new LibrarianDto(librarianRepository.save(librarian));
    }

    public Librarian login(LoginRequest request) throws Exception {
        var librarian = librarianRepository.findByEmail(request.email);

        if (librarian == null) {
            throw new ModelNotFoundException(Librarian.class, request.email);
        }
        return librarian;
    }
}
