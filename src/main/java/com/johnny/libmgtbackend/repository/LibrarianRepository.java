package com.johnny.libmgtbackend.repository;

import com.johnny.libmgtbackend.models.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarianRepository extends JpaRepository<Librarian, Long> {

    Librarian findByEmail(String email);
}
