package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.auth.AuthenticationProvider;
import com.johnny.libmgtbackend.dtos.BorrowRecordDto;
import com.johnny.libmgtbackend.dtos.LibrarianDto;
import com.johnny.libmgtbackend.exception.ModelNotFoundException;
import com.johnny.libmgtbackend.exception.UnauthorizedException;
import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.models.BorrowRecord;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.BorrowRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.request.UpdateLibrarianRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;
    @Autowired
    private PatronRepository patronRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PagedResourcesAssembler<LibrarianDto> pagedResourcesAssembler;

    public PagedModel<EntityModel<LibrarianDto>> getAllLibrarians(Pageable paging) {
        Page<Librarian> result = librarianRepository.findAll(paging);

        return pagedResourcesAssembler.toModel(result.map(LibrarianDto::new));
    }

    public LibrarianDto getLibrarian(Long id) {
        return new LibrarianDto(librarianRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Librarian.class, id)));
    }

    public LibrarianDto updateLibrarian(UpdateLibrarianRequest request, Long id) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();
        var librarian = librarianRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Librarian.class, id));

        if (!authLibrarian.getId().equals(librarian.getId())) {
            throw new UnauthorizedException("You cannot update another librarian's profile!");
        }

        if (request.email != null && !librarian.getEmail().equals(request.email)) {
            var existingLibrarian = librarianRepository.findByEmail(request.email);
            if (existingLibrarian != null) {
                throw new Exception("Librarian with email already exists");
            }
            librarian.setEmail(request.email);
        }
        if (request.name != null && !librarian.getName().equals(request.name)) {
            librarian.setName(request.name);
        }

        return new LibrarianDto(librarianRepository.save(librarian));
    }

    public void deleteLibrarian(Long id) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();
        if (!authLibrarian.getId().equals(id)) {
            throw new UnauthorizedException("You cannot delete another person's profile");
        }
        librarianRepository.deleteById(id);
    }

    public BorrowRecordDto borrowBook(Long bookId, Long patronId) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();

        var book = bookRepository.findById(bookId).orElseThrow(() -> new ModelNotFoundException(Book.class, bookId));
        var patron = patronRepository.findById(patronId).orElseThrow(() -> new ModelNotFoundException(Patron.class, patronId));

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);

        if (borrowRecord == null) {
            var newRecord = new BorrowRecord(book, patron, authLibrarian);
            newRecord.setBorrowDate(LocalDate.now());

            return new BorrowRecordDto(borrowRepository.save(newRecord));
        }
        throw new UnauthorizedException("You cannot borrow the same book twice");
    }

    public BorrowRecordDto returnBook(Long bookId, Long patronId) throws Exception {
        var authLibrarian = authenticationProvider.getAuthenticatedLibrarian();

        var book = bookRepository.findById(bookId).orElseThrow(() -> new ModelNotFoundException(Book.class, bookId));
        var patron = patronRepository.findById(patronId).orElseThrow(() -> new ModelNotFoundException(Patron.class, patronId));

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);

        if (borrowRecord != null) {
            if (!borrowRecord.getLibrarian().getId().equals(authLibrarian.getId())) {
                throw new UnauthorizedException("Kindly return the book to the Librarian that borrowed it to you");
            }
            borrowRecord.setReturnDate(LocalDate.now());
            return new BorrowRecordDto(borrowRepository.save(borrowRecord));
        }
        throw  new ModelNotFoundException(BorrowRecord.class, book.getTitle(), patron.getName());
    }
}
