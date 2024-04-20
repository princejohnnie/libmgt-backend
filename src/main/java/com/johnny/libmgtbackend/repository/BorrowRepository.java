package com.johnny.libmgtbackend.repository;

import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.models.BorrowRecord;
import com.johnny.libmgtbackend.models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<BorrowRecord, Long> {
    BorrowRecord findBorrowRecordByBookAndPatronAndReturnDateNull(Book book, Patron patron);
}
