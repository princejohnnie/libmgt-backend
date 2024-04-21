package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.ErrorDto;
import com.johnny.libmgtbackend.dtos.LibrarianDto;
import com.johnny.libmgtbackend.request.UpdateLibrarianRequest;
import com.johnny.libmgtbackend.service.LibrarianService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/librarians/{id}")
    ResponseEntity<Object> update(@RequestBody @Valid UpdateLibrarianRequest request, @PathVariable Long id) {
        try {
            var librarianDto = librarianService.updateLibrarian(request, id);
            return new ResponseEntity<>(librarianDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/librarians/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            librarianService.deleteLibrarian(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    ResponseEntity<Object> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            var borrowRecordDto = librarianService.borrowBook(bookId, patronId);
            return new ResponseEntity<>(borrowRecordDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    ResponseEntity<Object> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        try {
            var borrowRecordDto = librarianService.returnBook(bookId, patronId);
            return new ResponseEntity<>(borrowRecordDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
