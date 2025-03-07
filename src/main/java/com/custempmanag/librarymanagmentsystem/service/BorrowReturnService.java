package com.custempmanag.librarymanagmentsystem.service;

import com.custempmanag.librarymanagmentsystem.exception.CustomException;
import com.custempmanag.librarymanagmentsystem.exception.ResourceNotFoundException;
import com.custempmanag.librarymanagmentsystem.models.Books;
import com.custempmanag.librarymanagmentsystem.models.BorrowingRecords;
import com.custempmanag.librarymanagmentsystem.models.Patrons;
import com.custempmanag.librarymanagmentsystem.repository.BooksRepository;
import com.custempmanag.librarymanagmentsystem.repository.BorrowingRecordsRepository;
import com.custempmanag.librarymanagmentsystem.repository.PatronsRepository;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BorrowReturnService {
    @Autowired
    private BooksRepository bookRepository;
    @Autowired
    private PatronsRepository patronRepository;
    @Autowired
    private BorrowingRecordsRepository borrowingRecordRepository;

    @CacheEvict(value = "borrowCache", key = "'borrowedBooks'")
    @Transactional
    public MessageResponse borrowBook(Long bookId, Long patronId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Patrons patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Patron not found"));

        if (!book.isAvailable()) {
            throw new CustomException("Book is already borrowed");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        BorrowingRecords borrowingRecords = new BorrowingRecords(book, patron, LocalDate.now());

        borrowingRecordRepository.save(borrowingRecords);
        return new MessageResponse(HttpStatus.OK.toString(), "Book borrowed Successfully!", borrowingRecords);
    }

    @CacheEvict(value = "borrowCache", key = "'borrowedBooks'")
    @Transactional
    public MessageResponse returnBook(Long bookId, Long patronId) {
        BorrowingRecords record = borrowingRecordRepository.findAll()
                .stream()
                .filter(r -> r.getBook().getId().equals(bookId) && r.getPatron().getId().equals(patronId) && r.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new CustomException("No active borrowing record found"));

        record.setReturnDate(LocalDate.now());
        record.getBook().setAvailable(true);

        bookRepository.save(record.getBook());
        borrowingRecordRepository.save(record);
        return new MessageResponse(HttpStatus.OK.toString(), "Book returned Successfully!", record);
    }
}
