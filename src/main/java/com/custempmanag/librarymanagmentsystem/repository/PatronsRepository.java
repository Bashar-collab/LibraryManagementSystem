package com.custempmanag.librarymanagmentsystem.repository;

import com.custempmanag.librarymanagmentsystem.models.Patrons;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronsRepository extends JpaRepository<Patrons, Long> {

    boolean existsByName(String name);
}
