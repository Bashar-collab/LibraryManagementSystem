package com.custempmanag.librarymanagmentsystem.controller;

import com.custempmanag.librarymanagmentsystem.exception.CustomException;
import com.custempmanag.librarymanagmentsystem.exception.ResourceNotFoundException;
import com.custempmanag.librarymanagmentsystem.jwt.JwtUtil;
import com.custempmanag.librarymanagmentsystem.models.Books;
import com.custempmanag.librarymanagmentsystem.models.Patrons;
import com.custempmanag.librarymanagmentsystem.repository.BooksRepository;
import com.custempmanag.librarymanagmentsystem.repository.UserRepository;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import com.custempmanag.librarymanagmentsystem.service.BorrowReturnService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(BorrowReturnController.class)
//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BorrowReturnControllerTest {

//    @Autowired
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowReturnService borrowReturnService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void borrowBookShouldReturnCreatedTest() throws Exception {
        Long bookId = 1L;
        Long patronId = 2L;
        Books books = new Books();
        books.setId(bookId);
        books.setAvailable(true);
        Patrons patrons = new Patrons();
        patrons.setId(patronId);
        MessageResponse mockResponse = new MessageResponse(
                HttpStatus.OK.toString(),
                "Book borrowed Successfully!",
                null

        );

        String token = jwtUtil.generateToken("Spring");

        when(borrowReturnService.borrowBook(bookId, patronId)).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value("Book borrowed Successfully!"));
    }
        @Test
        void borrowBook_BookNotFoundTest() throws Exception {
            Long bookId = 1L;
            Long patronId = 1L;

            String token = jwtUtil.generateToken("Spring");

            when(borrowReturnService.borrowBook(bookId, patronId)).thenThrow(new ResourceNotFoundException("Book not found"));

            mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Book not found"));
        }

        @Test
        void borrowBook_BookAlreadyBorrowedTest() throws Exception {
            Long bookId = 1L;
            Long patronId = 1L;

            when(borrowReturnService.borrowBook(bookId, patronId)).thenThrow(new CustomException("Book is already borrowed"));

            String token = jwtUtil.generateToken("Spring");


            mockMvc.perform(MockMvcRequestBuilders.post("/api/borrow/{bookId}/patron/{patronId}", bookId, patronId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect( jsonPath("$.message").value("Book is already borrowed"));
        }

        @Test
        void returnBook_SuccessTest() throws Exception {
            Long bookId = 1L;
            Long patronId = 1L;

            MessageResponse expectedResponse = new MessageResponse(HttpStatus.OK.toString(), "Book returned Successfully!", null);

            String token = jwtUtil.generateToken("Spring");

            when(borrowReturnService.returnBook(bookId, patronId)).thenReturn(expectedResponse);

            mockMvc.perform(MockMvcRequestBuilders.post("/api/return/{bookId}/patron/{patronId}", bookId, patronId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                    .andExpect(jsonPath("$.message").value("Book returned Successfully!"));
        }

        @Test
        void returnBook_NoActiveRecordTest() throws Exception {
            Long bookId = 1L;
            Long patronId = 1L;

            String token = jwtUtil.generateToken("Spring");

            when(borrowReturnService.returnBook(bookId, patronId)).thenThrow(new CustomException("No active borrowing record found"));

            mockMvc.perform(MockMvcRequestBuilders.post("/api/return/{bookId}/patron/{patronId}", bookId, patronId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("No active borrowing record found"));
        }
    }

