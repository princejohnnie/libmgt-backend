package com.johnny.libmgtbackend.auth;

import com.johnny.libmgtbackend.models.Librarian;

import java.nio.file.AccessDeniedException;

public interface AuthenticationProvider {
    Librarian getAuthenticatedLibrarian() throws AccessDeniedException;
}
