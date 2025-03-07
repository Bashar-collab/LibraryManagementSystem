package com.custempmanag.librarymanagmentsystem.repository;

import com.custempmanag.librarymanagmentsystem.models.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<Books, Long> {
    boolean existsByIsbn(String isbn);
}
