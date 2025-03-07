package com.custempmanag.librarymanagmentsystem.controller;

import com.custempmanag.librarymanagmentsystem.repository.BooksRepository;
import com.custempmanag.librarymanagmentsystem.repository.PatronsRepository;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import com.custempmanag.librarymanagmentsystem.service.BorrowReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BorrowReturnController {
    @Autowired
    private BorrowReturnService borrowReturnService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<?> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId)
    {
        MessageResponse messageResponse = borrowReturnService.borrowBook(bookId, patronId);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/return/{bookId}/patron/{patronId}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId, @PathVariable Long patronId)
    {
        MessageResponse messageResponse = borrowReturnService.returnBook(bookId, patronId);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
}
