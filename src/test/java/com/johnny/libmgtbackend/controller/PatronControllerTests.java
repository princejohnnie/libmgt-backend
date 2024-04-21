package com.johnny.libmgtbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.libmgtbackend.auth.AuthenticationProvider;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.models.Patron;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.repository.PatronRepository;
import com.johnny.libmgtbackend.request.CreatePatronRequest;
import com.johnny.libmgtbackend.request.UpdatePatronRequest;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PatronControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LibrarianRepository librarianRepository;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SecurityFilterChain securityFilterChain;


    @Test
    void  contextLoads() {

    }


    @Test
    public void givenPatron_whenGetPatrons_thenReturnJsonArray() throws Exception {
        var patron = patronRepository.save(new Patron("John Uzodinma", "+234809382832"));

        mockMvc.perform(
                        get("/api/patrons")
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.items", hasSize(1)))
                .andExpect(jsonPath("$._embedded.items[0].name", is("John Uzodinma")))
                .andExpect(jsonPath("$._embedded.items[0].contact", is("+234809382832")));
    }

    @Test
    public void givenPatron_whenGetPatron_thenReturnJson() throws Exception {
        var patron = patronRepository.save(new Patron("John Uzodinma", "+234809382832"));

        mockMvc.perform(
                        get("/api/patrons/{id}", patron.getId())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Uzodinma")))
                .andExpect(jsonPath("$.contact", is("+234809382832")));
    }

    @Test
    public void givenPatron_whenPostPatron_thenStorePatron() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Uzodinma", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var request = new CreatePatronRequest("John Uzodinma", "+234809382832");
        var payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, patronRepository.count(), "Confirm that book was actually stored in the DB");
    }

    @Test
    @Transactional
    public void givenPatron_whenPutBook_thenUpdatePatron() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Uzodinma", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var patron = patronRepository.save(new Patron("John Uzodinma", "+234809382832"));
        var request = new UpdatePatronRequest("John Uzodinma updated", "+234809382123");

        var updatePayload = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        put("/api/patrons/{id}", patron.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePayload)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Uzodinma updated")))
                .andExpect(jsonPath("$.contact", is("+234809382123")));

        Assertions.assertEquals("John Uzodinma updated", patronRepository.findById(patron.getId()).get().getName(), "Confirm that Patron name was updated in the DB");
        Assertions.assertEquals("+234809382123", patronRepository.findById(patron.getId()).get().getContact(), "Confirm that book author was updated in the DB");
    }

    @Test
    public void givenPatron_whenDeletePatron_thenDeletePatron() throws Exception {
        var librarian = librarianRepository.save(new Librarian("johnny@gmail.com", "John Uzodinma", "password"));
        Mockito.when(authenticationProvider.getAuthenticatedLibrarian()).thenReturn(librarian);

        var patron = patronRepository.save(new Patron("John Uzodinma", "+234809382832"));
        Assertions.assertEquals(1, patronRepository.count(), "Confirm that Patron was saved to the DB");

        mockMvc.perform(
                delete("/api/patrons/{id}", patron.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, patronRepository.count(), "Confirm that Patron no longer exists in the DB");
    }
}
