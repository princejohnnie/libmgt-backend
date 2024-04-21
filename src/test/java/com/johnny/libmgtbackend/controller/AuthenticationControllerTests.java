package com.johnny.libmgtbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnny.libmgtbackend.models.Librarian;
import com.johnny.libmgtbackend.repository.LibrarianRepository;
import com.johnny.libmgtbackend.request.RegisterRequest;
import com.johnny.libmgtbackend.request.LoginRequest;
import com.johnny.libmgtbackend.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void  contextLoads() {

    }



    @Test
    public void givenToken_whenGetAuthenticatedLibrarian_thenReturnLibrarian() throws Exception {
        var librarian = librarianRepository.save(new Librarian("john@gmail.com", "John Uzodinma", "password"));
        var token = tokenService.generateToken(librarian.getId());

        mockMvc.perform(
                get("/api/auth")
                        .header("Authorization", "Bearer " + token)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john@gmail.com")))
                .andExpect(jsonPath("$.name", is("John Uzodinma")));
    }

    @Test
    public void givenRequest_whenRegisterLibrarian_thenCreateLibrarian() throws Exception {
        var request = new RegisterRequest("john@gmail.com", "John Prince", "password");

        var payload = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, librarianRepository.count(), "Confirm that Librarian was actually stored in the DB");

    }

    @Test
    public void givenRequest_whenLoginLibrarian_thenReturnToken() throws Exception {
        var librarian = librarianRepository.save(new Librarian("john@gmail.com", "John Uzodinma", "password"));

        var request = new LoginRequest("john@gmail.com", "password");

        var payload = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
        ).andExpect(status().isOk());

        String token = resultActions.andReturn().getResponse().getContentAsString();
        var tokenData = tokenService.validateToken(token);

        var loggedInLibrarian = librarianRepository.findById(tokenData.getLibrarianId()).get();

        Assertions.assertEquals(request.email, loggedInLibrarian.getEmail(), "Confirm that the email in the token returned actually belongs to the logged in librarian");
    }

}