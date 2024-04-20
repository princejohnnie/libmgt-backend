package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/books")
    List<Book> index() {
        return bookRepository.findAll();
    }

    @GetMapping("/books/{id}")
    Book show(@PathVariable Long id) {
        return bookRepository.findById(id).get();
    }

    @PostMapping("/books")
    Book store(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/books/{id}")
    Book update(@RequestBody Book newBook, @PathVariable Long id) {
        var book = bookRepository.findById(id).get();

        if (!book.getTitle().equals(newBook.getTitle())) {
            book.setTitle(newBook.getTitle());
        }
        if (!book.getAuthor().equals(newBook.getAuthor())) {
            book.setAuthor(newBook.getAuthor());
        }
        if (!book.getIsbn().equals(newBook.getIsbn())) {
            book.setIsbn(newBook.getIsbn());
        }
        if (!book.getPublicationYear().equals(newBook.getPublicationYear())) {
            book.setPublicationYear(newBook.getPublicationYear());
        }

        return bookRepository.save(book);
    }

    @DeleteMapping("/books/{id}")
    void delete(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
