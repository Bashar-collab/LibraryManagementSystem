package com.custempmanag.librarymanagmentsystem.repository;

import com.custempmanag.librarymanagmentsystem.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
}
