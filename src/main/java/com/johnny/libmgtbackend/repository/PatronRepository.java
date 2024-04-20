package com.johnny.libmgtbackend.repository;

import com.johnny.libmgtbackend.models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}
