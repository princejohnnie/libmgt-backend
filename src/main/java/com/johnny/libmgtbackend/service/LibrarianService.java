package com.johnny.libmgtbackend.service;

import com.johnny.libmgtbackend.dtos.BorrowRecordDto;
import com.johnny.libmgtbackend.dtos.LibrarianDto;
import com.johnny.libmgtbackend.models.BorrowRecord;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.BorrowRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
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
        return new LibrarianDto(librarianRepository.findById(id).get());
    }

    public LibrarianDto createLibrarian(Librarian librarian) {
        return new LibrarianDto(librarianRepository.save(librarian));
    }

    public LibrarianDto updateLibrarian(Librarian newLibrarian, Long id) {
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

        return new LibrarianDto(librarianRepository.save(user));
    }

    public void deleteLibrarian(Long id) {
        librarianRepository.deleteById(id);
    }

    public BorrowRecordDto borrowBook(Long bookId, Long patronId) {
        var patron = patronRepository.findById(patronId).get();
        var book = bookRepository.findById(bookId).get();

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);
        if (borrowRecord == null) {
            var newRecord = new BorrowRecord(book, patron);
            newRecord.setBorrowDate(LocalDate.now());

            return new BorrowRecordDto(borrowRepository.save(newRecord));
        }
        return null;
    }

    public BorrowRecordDto returnBook(Long bookId, Long patronId) {
        var patron = patronRepository.findById(patronId).get();
        var book = bookRepository.findById(bookId).get();

        var borrowRecord = borrowRepository.findBorrowRecordByBookAndPatronAndReturnDateNull(book, patron);
        if (borrowRecord != null) {
            borrowRecord.setReturnDate(LocalDate.now());
            return new BorrowRecordDto(borrowRepository.save(borrowRecord));
        }
        return null;
    }
}
