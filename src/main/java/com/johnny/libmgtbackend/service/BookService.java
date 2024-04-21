package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.auth.AuthenticationProvider;
import com.johnny.libmgtbackend.dtos.BookDto;
import com.johnny.libmgtbackend.exception.ModelNotFoundException;
import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.request.AddBookRequest;
import com.johnny.libmgtbackend.request.UpdateBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PagedResourcesAssembler<BookDto> pagedResourcesAssembler;

    public PagedModel<EntityModel<BookDto>> getAllBooks(Pageable paging) {
        Page<Book> result = bookRepository.findAll(paging);
        return pagedResourcesAssembler.toModel(result.map(BookDto::new));
    }

    public BookDto getBook(Long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Book.class, id));
        return new BookDto(book);
    }

    public BookDto addBook(AddBookRequest request) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();

        var book = new Book(request.title, request.author, request.isbn, request.publicationYear);
        return new BookDto(bookRepository.save(book));
    }

    @Transactional
    public BookDto updateBook(UpdateBookRequest request, Long id) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();

        var book = bookRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Book.class, id));

        if (request.title != null && !book.getTitle().equals(request.title)) {
            book.setTitle(request.title);
        }
        if (request.author != null && !book.getAuthor().equals(request.author)) {
            book.setAuthor(request.author);
        }
        if (request.isbn != null && !book.getIsbn().equals(request.isbn)) {
            book.setIsbn(request.isbn);
        }
        if (request.publicationYear != null && !book.getPublicationYear().equals(request.publicationYear)) {
            book.setPublicationYear(request.publicationYear);
        }

        return new BookDto(bookRepository.save(book));
    }

    public void deleteBook(Long id) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();
        bookRepository.deleteById(id);
    }
}
