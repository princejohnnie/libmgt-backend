package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.models.BorrowRecord;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.BorrowRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    private BorrowRepository borrowRepository;


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

        if (!user.getEmail().equals(newLibrarian.getEmail())) {
            user.setEmail(newLibrarian.getEmail());
        }
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
    BorrowRecord borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        var patron = patronRepository.findById(patronId).get();
        var book = bookRepository.findById(bookId).get();

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);
        if (borrowRecord == null) {
            var newRecord = new BorrowRecord(book, patron);
            newRecord.setBorrowDate(LocalDate.now());

            return borrowRepository.save(newRecord);
        }
        return null;

    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    BorrowRecord returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        var patron = patronRepository.findById(patronId).get();
        var book = bookRepository.findById(bookId).get();

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);
        if (borrowRecord != null) {
            borrowRecord.setReturnDate(LocalDate.now());
            return borrowRepository.save(borrowRecord);
        }
        return null;
    }
}
