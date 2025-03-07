package com.custempmanag.librarymanagmentsystem.controller;

import com.custempmanag.librarymanagmentsystem.dto.PatronsDTO;
import com.custempmanag.librarymanagmentsystem.exception.CustomException;
import com.custempmanag.librarymanagmentsystem.jwt.JwtUtil;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import com.custempmanag.librarymanagmentsystem.service.PatronsService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Array.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
//@WebMvcTest(PatronsController.class)
@SpringBootTest
@AutoConfigureMockMvc
class PatronsControllerTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronsService patronsService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
    }
    @Test
    void getAllPatronsTest() throws Exception {
        PatronsDTO patronsDTO1 = new PatronsDTO(1L, "Patron 1", "Contact info 1");
        PatronsDTO patronsDTO2 = new PatronsDTO(2L, "Patron 2", "Contact info 2");
        List<PatronsDTO> patronsDTOList = new ArrayList();
        patronsDTOList.add(patronsDTO1);
        patronsDTOList.add(patronsDTO2);

        MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.toString(), "Patrons retrieved successfully!", patronsDTOList);

        String token = jwtUtil.generateToken("Spring");

        when(patronsService.getAllPatrons()).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons")
                .header("Authorization", "Bearer " + token)) // need to check this
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Patrons retrieved successfully!"))
                .andExpect(jsonPath("$.data[0].name").value("Patron 1"))
                .andExpect((ResultMatcher) jsonPath("$.data[0].contactInfo").value("Contact info 1"))
                .andExpect((ResultMatcher) jsonPath("$.data[1].name").value("Patron 2"))
                .andExpect((ResultMatcher) jsonPath("$.data[1].contactInfo").value("Contact info 2"));
    }

    @Test
    void getPatronByIdTest() throws Exception {
        PatronsDTO patronsDTO = new PatronsDTO(1L, "Patron 1", "Contact info 1");
        MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.toString(), "Patron retrieved successfully!", patronsDTO);

        String token = jwtUtil.generateToken("Spring");

        when(patronsService.getPatronById(1L)).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/{id}", 1L)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Patron retrieved successfully!"))
                .andExpect(jsonPath("$.data.name").value("Patron 1"));
    }

    @Test
    void addPatronTest() throws Exception {
        PatronsDTO patronsDTO  = new PatronsDTO("Bashar", "Contact info 1");
        MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.toString(), "Patron added successfully!", patronsDTO);

        String token = jwtUtil.generateToken("Spring");

        when(patronsService.addPatron(patronsDTO)).thenReturn(messageResponse);

//        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(patronsDTO)))
//                .andExpect(status().isCreated())
//                .andDo(print())  // This will print the response body
//                .andExpect(jsonPath("$.status").value("201 CREATED"))
//                .andExpect(jsonPath("$.message").value("Patron added successfully!"))
//                .andExpect(jsonPath("$.data.name").value("Patron 1"));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/patrons")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patronsDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("201 CREATED"))
                .andExpect(jsonPath("$.message").value("Patron added successfully!"))
                .andExpect(jsonPath("$.data.name").value("Patron 1"));
    }

    @Test
    void updatePatronTest() throws Exception {
        Long id = 1L;
        PatronsDTO patronsDTO = new PatronsDTO( "Updated Patron", "Updated Contact info");
        MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.toString(), "Patron updated successfully!", patronsDTO);

        String token = jwtUtil.generateToken("Spring");

        when(patronsService.updatePatron(id, patronsDTO)).thenReturn(messageResponse);

//        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/{id}", id)
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(patronsDTO)))
//                .andDo(result -> {
//                    String responseBody = result.getResponse().getContentAsString();
//                    System.out.println("Response Body: " + responseBody);
//                });

        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patronsDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Patron updated  successfully!"))
                .andExpect(jsonPath("$.data.name").value("Updated Patron"));

    }

    @Test
    void deleteBookTest() throws Exception {
        Long id = 1L;
        PatronsDTO patronsDTO = new PatronsDTO( id, "Patron 1", "Contact info 1");
        MessageResponse messageResponse = new MessageResponse(HttpStatus.OK.toString(), "Patron deleted successfully!", patronsDTO);

        String token = jwtUtil.generateToken("Spring");

        when(patronsService.deletePatron(id)).thenReturn(messageResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patrons/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.message").value("Patron deleted successfully!"));
//                .andExpect((ResultMatcher) jsonPath("$.data[0].name").value("Patron 1"));
    }

    @Test
    public void testPatronNotFound() throws Exception {
        Long id = 1L;

        when(patronsService.getPatronById(id)).thenThrow(new CustomException("Patron not found!"));

        String token = jwtUtil.generateToken("Spring");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/{id}", id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Patron not found!"));
    }

}