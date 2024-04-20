package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.dtos.BookDto;
import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PagedResourcesAssembler<BookDto> pagedResourcesAssembler;

    public PagedModel<EntityModel<BookDto>> getAllBooks(Pageable paging) {
        Page<Book> result = bookRepository.findAll(paging);
        return pagedResourcesAssembler.toModel(result.map(BookDto::new));
    }

    public BookDto getBook(Long id) {
        return new BookDto(bookRepository.findById(id).get());
    }

    public BookDto addBook(Book book) {
        return new BookDto(bookRepository.save(book));
    }

    public BookDto updateBook(Book newBook, Long id) {
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

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
