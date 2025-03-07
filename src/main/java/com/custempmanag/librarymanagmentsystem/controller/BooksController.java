package com.custempmanag.librarymanagmentsystem.controller;

import com.custempmanag.librarymanagmentsystem.dto.BooksDTO;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import com.custempmanag.librarymanagmentsystem.service.BooksService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<?> getAllBooks() {
        MessageResponse messageResponse = booksService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        MessageResponse messageResponse = booksService.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addBook(@Valid @RequestBody BooksDTO booksDTO) {
        MessageResponse messageResponse = booksService.addBook(booksDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BooksDTO booksDTO) {
        MessageResponse messageResponse = booksService.updateBook(id, booksDTO);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        MessageResponse messageResponse = booksService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
}
