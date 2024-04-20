package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.BookDto;
import com.johnny.libmgtbackend.request.AddBookRequest;
import com.johnny.libmgtbackend.request.UpdateBookRequest;
import com.johnny.libmgtbackend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    PagedModel<EntityModel<BookDto>> index(
            @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"publicationYear"}) Pageable paging) {

        return bookService.getAllBooks(paging);
    }

    @GetMapping("/books/{id}")
    BookDto show(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PostMapping("/books")
    BookDto store(@RequestBody @Valid AddBookRequest request) {
        return bookService.addBook(request);
    }

    @PutMapping("/books/{id}")
    BookDto update(@RequestBody UpdateBookRequest request, @PathVariable Long id) {
        return bookService.updateBook(request, id);
    }

    @DeleteMapping("/books/{id}")
    void delete(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
