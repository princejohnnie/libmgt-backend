package com.johnny.libmgtbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.libmgtbackend.auth.AuthenticationProvider;
import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.models.BorrowRecord;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.BorrowRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.request.UpdateLibrarianRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LibrarianControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SecurityFilterChain securityFilterChain;


    @Test
    void  contextLoads() {

    }


    @Test
    public void givenLibrarian_whenGetLibrarians_thenReturnJsonArray() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Uzodinma", "password"));

        mockMvc.perform(
                        get("/api/librarians")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.items", hasSize(1)))
                .andExpect(jsonPath("$._embedded.items[0].email", is("johnny@gmail.com")))
                .andExpect(jsonPath("$._embedded.items[0].name", is("John Uzodinma")));
    }

    @Test
    public void givenLibrarian_whenGetLibrarian_thenReturnJson() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Uzodinma", "password"));

        mockMvc.perform(
                        get("/api/librarians/{id}", librarian.getId())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("johnny@gmail.com")))
                .andExpect(jsonPath("$.name", is("John Uzodinma")));
    }

    @Test
    @Transactional
    public void givenLibrarian_whenPutLibrarian_thenUpdateLibrarian() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Uzodinma", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var request = new UpdateLibrarianRequest("johnny123@gmail.com", "John Prince", "password");

        var updatePayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        put("/api/librarians/{id}", librarian.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePayload)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("johnny123@gmail.com")))
                .andExpect(jsonPath("$.name", is("John Prince")));

        Assertions.assertEquals("johnny123@gmail.com", librarianRepository.findById(librarian.getId()).get().getEmail(), "Confirm that Librarian email was updated in the DB");
        Assertions.assertEquals("John Prince", librarianRepository.findById(librarian.getId()).get().getName(), "Confirm that Librarian name was updated in the DB");
    }

    @Test
    public void givenLibrarian_whenDeleteLibrarian_thenDeleteLibrarian() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Uzodinma", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        Assertions.assertEquals(1, librarianRepository.count(), "Confirm that Librarian was saved to the DB");

        mockMvc.perform(
                delete("/api/librarians/{id}", librarian.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, librarianRepository.count(), "Confirm that Patron no longer exists in the DB");
    }

    @Test
    @Transactional
    public void givenBookAndPatron_whenPostBorrowBook_thenReturnBookRecord() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Prince", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var patron = patronRepository.save(new Patron("John Uzodinma", "+234809382832"));
        var book = bookRepository.save(new Book("Game of Thrones", "John Wick", "1288-2828-2228", "2016"));

        var borrowRecord = new BorrowRecord(book, patron, librarian);

        mockMvc.perform(
                post("/api/borrow/{bookId}/patron/{patronId}", book.getId(), patron.getId())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowDate", is(LocalDate.now().toString())));

        Assertions.assertEquals(1, borrowRepository.count(), "Confirm that borrow record was actually stored in the DB");

    }

    @Test
    @Transactional
    public void givenBookAndPatron_whenPutReturnBook_thenReturnBookRecord() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Prince", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var patron = patronRepository.save(new Patron("John Uzodinma", "+234809382832"));
        var book = bookRepository.save(new Book("Game of Thrones", "John Wick", "1288-2828-2228", "2016"));

        mockMvc.perform(
                post("/api/borrow/{bookId}/patron/{patronId}", book.getId(), patron.getId())
        ).andExpect(status().isOk());

        mockMvc.perform(
                        put("/api/return/{bookId}/patron/{patronId}", book.getId(), patron.getId())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.returnDate", is(LocalDate.now().toString())));

    }

}
