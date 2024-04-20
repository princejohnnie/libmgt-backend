package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibrarianController {
    @Autowired
    private LibrarianRepository librarianRepository;
    @Autowired
    private PatronRepository patronRepository;
    @Autowired
    private BookRepository bookRepository;


    @GetMapping("/librarians")
    List<Librarian> index() {
        return librarianRepository.findAll();
    }

    @GetMapping("/librarians/{id}")
    Librarian show(@PathVariable Long id) {
        return librarianRepository.findById(id).get();
    }

    @PostMapping("/librarians")
    Librarian store(@RequestBody Librarian librarian) {
        return librarianRepository.save(librarian);
    }

    @PutMapping("/librarians/{id}")
    Librarian update(@RequestBody Librarian newLibrarian, @PathVariable Long id) {
        var user = librarianRepository.findById(id).get();

        if (!user.getName().equals(newLibrarian.getName())) {
            user.setName(newLibrarian.getName());
        }
        if (!user.getPassword().equals(newLibrarian.getPassword())) {
            user.setPassword(newLibrarian.getPassword());
        }

        return librarianRepository.save(user);
    }

    @DeleteMapping("/librarians/{id}")
    void delete(@PathVariable Long id) {
        librarianRepository.deleteById(id);
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    Patron borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        var patron = patronRepository.findById(patronId).get();
        var book = bookRepository.findById(bookId).get();

        patron.getBooks().add(book);

        return patronRepository.save(patron);

    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    Patron returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        var patron = patronRepository.findById(patronId).get();
        var book = bookRepository.findById(bookId).get();

        if (!patron.getBooks().isEmpty()) {
            patron.getBooks().remove(book);
        }

        return patronRepository.save(patron);
    }
}
