package com.johnny.libmgtbackend.dtos;

import com.johnny.libmgtbackend.models.BorrowRecord;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Relation(collectionRelation = "items")
public class BorrowRecordDto {
    public Long id;
    public LocalDate borrowDate;
    public LocalDate returnDate;
    public BookDto book;
    public PatronDto patron;
    public LibrarianDto librarian;

    public BorrowRecordDto(BorrowRecord borrowRecord) {
        this.id = borrowRecord.getId();
        this.borrowDate = borrowRecord.getBorrowDate();
        this.returnDate = borrowRecord.getReturnDate();
        this.book = new BookDto(borrowRecord.getBook());
        this.patron = new PatronDto(borrowRecord.getPatron());
        this.librarian = new LibrarianDto(borrowRecord.getLibrarian());
    }
}
