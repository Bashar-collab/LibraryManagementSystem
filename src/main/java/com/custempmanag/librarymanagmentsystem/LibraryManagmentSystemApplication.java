package com.custempmanag.librarymanagmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
public class LibraryManagmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagmentSystemApplication.class, args);
    }

}
