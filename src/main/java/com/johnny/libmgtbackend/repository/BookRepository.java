package com.johnny.libmgtbackend.repository;

import com.johnny.libmgtbackend.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
