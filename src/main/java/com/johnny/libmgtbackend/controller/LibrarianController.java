package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.BorrowRecordDto;
import com.johnny.libmgtbackend.dtos.LibrarianDto;
import com.johnny.libmgtbackend.models.BorrowRecord;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.BorrowRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.service.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class LibrarianController {

    @Autowired
    private LibrarianService librarianService;

    @GetMapping("/librarians")
    PagedModel<EntityModel<LibrarianDto>> index(
            @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"email"}) Pageable paging) {

        return librarianService.getAllLibrarians(paging);
    }

    @GetMapping("/librarians/{id}")
    LibrarianDto show(@PathVariable Long id) {
        return librarianService.getLibrarian(id);
    }

    @PostMapping("/librarians")
    LibrarianDto store(@RequestBody Librarian librarian) {
        return librarianService.createLibrarian(librarian);
    }

    @PutMapping("/librarians/{id}")
    LibrarianDto update(@RequestBody Librarian newLibrarian, @PathVariable Long id) {
        return librarianService.updateLibrarian(newLibrarian, id);
    }

    @DeleteMapping("/librarians/{id}")
    void delete(@PathVariable Long id) {
        librarianService.deleteLibrarian(id);
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    BorrowRecordDto borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return librarianService.borrowBook(bookId, patronId);

    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    BorrowRecordDto returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        return librarianService.returnBook(bookId, patronId);
    }
}
