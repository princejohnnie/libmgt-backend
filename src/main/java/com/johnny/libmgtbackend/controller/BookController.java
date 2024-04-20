package com.johnny.libmgtbackend.controller;

import com.johnny.libmgtbackend.dtos.BookDto;
import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PagedResourcesAssembler<BookDto> pagedResourcesAssembler;

    @GetMapping("/books")
    PagedModel<EntityModel<BookDto>> index(
            @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"publicationYear"}) Pageable paging) {

        Page<Book> result = bookRepository.findAll(paging);

        return pagedResourcesAssembler.toModel(result.map(BookDto::new));
    }

    @GetMapping("/books/{id}")
    BookDto show(@PathVariable Long id) {
        return new BookDto(bookRepository.findById(id).get());
    }

    @PostMapping("/books")
    BookDto store(@RequestBody Book book) {
        return new BookDto(bookRepository.save(book));
    }

    @PutMapping("/books/{id}")
    BookDto update(@RequestBody Book newBook, @PathVariable Long id) {
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

        return new BookDto(bookRepository.save(book));
    }

    @DeleteMapping("/books/{id}")
    void delete(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
