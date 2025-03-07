package com.custempmanag.librarymanagmentsystem.models;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "borrowing_records")
public class BorrowingRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Books book;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "patron_id")
    private Patrons patron;

    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowingRecords(Books book, Patrons patron, LocalDate borrowDate) {
        this.book = book;
        this.patron = patron;
        this.borrowDate = borrowDate;
    }

    public BorrowingRecords() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Books getBook() {
        return book;
    }

    public void setBook(Books book) {
        this.book = book;
    }

    public Patrons getPatron() {
        return patron;
    }

    public void setPatron(Patrons patron) {
        this.patron = patron;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
}
