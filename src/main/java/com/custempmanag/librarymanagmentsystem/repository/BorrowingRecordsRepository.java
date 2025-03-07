package com.custempmanag.librarymanagmentsystem.repository;

import com.custempmanag.librarymanagmentsystem.models.BorrowingRecords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRecordsRepository extends JpaRepository<BorrowingRecords, Long> {
}
