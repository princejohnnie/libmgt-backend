package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.dtos.BorrowRecordDto;
import com.johnny.libmgtbackend.dtos.LibrarianDto;
import com.johnny.libmgtbackend.exception.ModelNotFoundException;
import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.models.BorrowRecord;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.BorrowRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.request.CreateLibrarianRequest;
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
    private PagedResourcesAssembler<LibrarianDto> pagedResourcesAssembler;

    public PagedModel<EntityModel<LibrarianDto>> getAllLibrarians(Pageable paging) {
        Page<Librarian> result = librarianRepository.findAll(paging);

        return pagedResourcesAssembler.toModel(result.map(LibrarianDto::new));
    }

    public LibrarianDto getLibrarian(Long id) {
        return new LibrarianDto(librarianRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Librarian.class, id)));
    }

    public LibrarianDto createLibrarian(CreateLibrarianRequest request) {
        var librarian = new Librarian(request.email, request.name, request.password);
        return new LibrarianDto(librarianRepository.save(librarian));
    }

    public LibrarianDto updateLibrarian(UpdateLibrarianRequest request, Long id) {
        var user = librarianRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Librarian.class, id));

        if (request.email != null && !user.getEmail().equals(request.email)) {
            user.setEmail(request.email);
        }
        if (request.name != null && !user.getName().equals(request.name)) {
            user.setName(request.name);
        }
        if (request.password != null && !user.getPassword().equals(request.password)) {
            user.setPassword(request.password);
        }

        return new LibrarianDto(librarianRepository.save(user));
    }

    public void deleteLibrarian(Long id) {
        librarianRepository.deleteById(id);
    }

    public BorrowRecordDto borrowBook(Long bookId, Long patronId) {
        var book = bookRepository.findById(bookId).orElseThrow(() -> new ModelNotFoundException(Book.class, bookId));
        var patron = patronRepository.findById(patronId).orElseThrow(() -> new ModelNotFoundException(Patron.class, patronId));

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);
        if (borrowRecord == null) {
            var newRecord = new BorrowRecord(book, patron);
            newRecord.setBorrowDate(LocalDate.now());

            return new BorrowRecordDto(borrowRepository.save(newRecord));
        }
        return null;
    }

    public BorrowRecordDto returnBook(Long bookId, Long patronId) {
        var book = bookRepository.findById(bookId).orElseThrow(() -> new ModelNotFoundException(Book.class, bookId));
        var patron = patronRepository.findById(patronId).orElseThrow(() -> new ModelNotFoundException(Patron.class, patronId));

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);
        if (borrowRecord != null) {
            borrowRecord.setReturnDate(LocalDate.now());
            return new BorrowRecordDto(borrowRepository.save(borrowRecord));
        }
        return null;
    }
}
