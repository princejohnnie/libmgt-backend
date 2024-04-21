package com.johnny.libmgtbackend.auth;

import com.johnny.libmgtbackend.exception.ModelNotFoundException;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class BearerTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    public Librarian getAuthenticatedLibrarian() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("You do not have permission to perform yhis oprtation");
        }
        var librarianId = (Long) authentication.getPrincipal();
        return librarianRepository.findById(librarianId).orElseThrow(() -> new ModelNotFoundException(Librarian.class, librarianId));
    }
}
