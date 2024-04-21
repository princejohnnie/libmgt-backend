package com.johnny.libmgtbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.libmgtbackend.auth.AuthenticationProvider;
import com.johnny.libmgtbackend.models.Book;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.repository.BookRepository;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.request.AddBookRequest;
import com.johnny.libmgtbackend.request.UpdateBookRequest;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

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
    public void givenBook_whenGetBooks_thenReturnJsonArray() throws Exception {
        var book = bookRepository.save(new Book("Purple Hibiscus", "Chimamanda Adichie", "1288-2828-2228", "2016"));

        mockMvc.perform(
                get("/api/books")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.items", hasSize(1)))
                .andExpect(jsonPath("$._embedded.items[0].title", is("Purple Hibiscus")))
                .andExpect(jsonPath("$._embedded.items[0].author", is("Chimamanda Adichie")))
                .andExpect(jsonPath("$._embedded.items[0].isbn", is("1288-2828-2228")));
    }

    @Test
    public void givenBook_whenGetBook_thenReturnJson() throws Exception {
        var book = bookRepository.save(new Book("Purple Hibiscus", "Chimamanda Adichie", "1288-2828-2228", "2016"));

        mockMvc.perform(
                        get("/api/books/{id}", book.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Purple Hibiscus")))
                .andExpect(jsonPath("$.author", is("Chimamanda Adichie")))
                .andExpect(jsonPath("$.isbn", is("1288-2828-2228")));
    }

    @Test
    public void givenBook_whenPostBook_thenStoreBook() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Prince", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var request = new AddBookRequest("Purple Hibiscus", "Chimamanda Adichie", "1288-2828-2228", "2016");
        var payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, bookRepository.count(), "Confirm that book was actually stored in the DB");
    }

    @Test
    public void givenBook_whenPutBook_thenUpdateBook() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Prince", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var book = bookRepository.save(new Book("Game of Thrones", "John Wick", "1288-2828-2228", "2016"));
        var updateBook = new UpdateBookRequest("Game of Thrones updated", "John Walker", "1233-3423-4545", "2018");

        var updatePayload = objectMapper.writeValueAsString(updateBook);

        mockMvc.perform(
                put("/api/books/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Game of Thrones updated")));

        Assertions.assertEquals("Game of Thrones updated", bookRepository.findById(book.getId()).get().getTitle(), "Confirm that book title was updated in the DB");
        Assertions.assertEquals("John Walker", bookRepository.findById(book.getId()).get().getAuthor(), "Confirm that book author was updated in the DB");
    }

    @Test
    public void givenBook_whenDeleteBook_thenDeleteBook() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Prince", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var book = bookRepository.save(new Book("Game of Thrones", "John Wick", "1288-2828-2228", "2016"));
        Assertions.assertEquals(1, bookRepository.count(), "Confirm that book was saved to the DB");

        mockMvc.perform(
                        delete("/api/books/{id}", book.getId())
                ).andExpect(status().isOk());

        Assertions.assertEquals(0, bookRepository.count(), "Confirm that book no longer exists in the DB");
    }

}
