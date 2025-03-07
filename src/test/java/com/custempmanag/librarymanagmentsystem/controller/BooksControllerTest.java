package com.custempmanag.librarymanagmentsystem.controller;

import com.custempmanag.librarymanagmentsystem.dto.BooksDTO;
import com.custempmanag.librarymanagmentsystem.exception.CustomException;
import com.custempmanag.librarymanagmentsystem.jwt.JwtUtil;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import com.custempmanag.librarymanagmentsystem.service.BooksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(BooksController.class)
@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksService booksService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }
    @Test
    void getAllBooksTest() throws Exception {
        BooksDTO booksDTO = new BooksDTO(1L, "test title", "test author", 2002, "1234567890123", true);
        List<BooksDTO> booksDTOList = new ArrayList<>();
        booksDTOList.add(booksDTO);

        MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.toString(), "Books retrieved successfully!", booksDTOList);

        String token = jwtUtil.generateToken("Spring");

        when(booksService.getAllBooks()).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Books retrieved successfully!"))
                .andExpect(jsonPath("$.data[0].title").value("test title"))
                .andExpect(jsonPath("$.data[0].author").value("test author"))
                .andExpect(jsonPath("$.data[0].publicationYear").value(2002));
    }

    @Test
    void getBookByIdTest() throws Exception {
        BooksDTO booksDTO = new BooksDTO(1L, "test title", "test author", 2002, "1234567890123", true);
        MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.toString(), "Books retrieved successfully!", booksDTO);

        String token = jwtUtil.generateToken("Spring");

        when(booksService.getBookById(1L)).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", 1L)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Books retrieved successfully!"))
                .andExpect(jsonPath("$.data.title").value("test title"))
                .andExpect(jsonPath("$.data.author").value("test author"))
                .andExpect(jsonPath("$.data.publicationYear").value(2002));
    }

    @Test
    void addBookTest() throws Exception {
        BooksDTO booksDTO = new BooksDTO(1L, "test  title", "test author", 2002, "1234567890123", true);
        MessageResponse messageResponse =  new MessageResponse(HttpStatus.OK.toString(), "Books retrieved successfully!", booksDTO);
        String token = jwtUtil.generateToken("Spring");
        when(booksService.addBook(booksDTO)).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booksDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("200 CREATED"))
                .andExpect(jsonPath("$.message").value("Books added successfully!"))
                .andExpect(jsonPath("$.data[0].title").value("test title"))
                .andExpect(jsonPath("$.data[0].author").value("test author"))
                .andExpect(jsonPath("$.data[0].published").value(2002));
    }

    @Test
    void updateBookTest() throws Exception {
        Long id = 1L;
        BooksDTO booksDTO = new BooksDTO("Updated title", "updated author", 2000, "1234567890124", true);
        MessageResponse messageResponse =  new MessageResponse(HttpStatus.OK.toString(), "Books retrieved successfully!", booksDTO);
        String token = jwtUtil.generateToken("Spring");

        when(booksService.updateBook(id, booksDTO)).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booksDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Books updated successfully!"))
                .andExpect(jsonPath("$.data[0].title").value("test title"))
                .andExpect(jsonPath("$.data[0].author").value("test author"))
                .andExpect(jsonPath("$.data[0].published").value(2002));
    }

    @Test
    void deleteBookTest() throws Exception {
        Long id = 1L;
        BooksDTO booksDTO = new BooksDTO(id, "test  title", "test author", 2002, "1234567890123", true);
        MessageResponse messageResponse =  new MessageResponse(HttpStatus.OK.toString(), "Books deleted successfully!", booksDTO);
        String token = jwtUtil.generateToken("Spring");
        when(booksService.deleteBook(id)).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Books deleted successfully!"));
//                .andExpect(jsonPath("$.data[0].title").value("test title"))
//                .andExpect(jsonPath("$.data[0].author").value("test author"))
//                .andExpect(jsonPath("$.data[0].published").value(2002));
    }

    @Test
    public void testBookNotFound() throws Exception {
        Long id = 1L;

        when(booksService.getBookById(id)).thenThrow(new CustomException("Book not found!"));

        String token = jwtUtil.generateToken("Spring");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{id}", id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Book not found!"));
    }
}