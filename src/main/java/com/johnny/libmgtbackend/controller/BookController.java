package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.BookDto;
import com.johnny.libmgtbackend.dtos.ErrorDto;
import com.johnny.libmgtbackend.request.AddBookRequest;
import com.johnny.libmgtbackend.request.UpdateBookRequest;
import com.johnny.libmgtbackend.service.BookService;
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
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    ResponseEntity<PagedModel<EntityModel<BookDto>>> index(
            @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"publicationYear"}) Pageable paging) {

        return new ResponseEntity<>(bookService.getAllBooks(paging), HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    ResponseEntity<BookDto> show(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
    }

    @PostMapping("/books")
    ResponseEntity<?> store(@RequestBody @Valid AddBookRequest request) {
        try {
            var bookDto = bookService.addBook(request);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/books/{id}")
    ResponseEntity<Object> update(@RequestBody UpdateBookRequest request, @PathVariable Long id) {
        try {
            var bookDto = bookService.updateBook(request, id);
            return new ResponseEntity<>(bookDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/books/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
