package com.custempmanag.librarymanagmentsystem.controller;

import com.custempmanag.librarymanagmentsystem.dto.BooksDTO;
import com.custempmanag.librarymanagmentsystem.dto.PatronsDTO;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import com.custempmanag.librarymanagmentsystem.service.PatronsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patrons")
public class PatronsController {

   private final PatronsService patronsService;

   public PatronsController(PatronsService patronsService) {
       this.patronsService = patronsService;
   }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<?> getAllPatrons() {
        MessageResponse messageResponse = patronsService.getAllPatrons();
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
    public ResponseEntity<?> getPatronById(@PathVariable Long id) {
        MessageResponse messageResponse = patronsService.getPatronById(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPatron(@Valid @RequestBody PatronsDTO patronsDTO) {
        MessageResponse messageResponse = patronsService.addPatron(patronsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePatron(@PathVariable Long id, @Valid @RequestBody PatronsDTO patronsDTO) {
        MessageResponse messageResponse = patronsService.updatePatron(id, patronsDTO);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePatron(@PathVariable Long id) {
        MessageResponse messageResponse = patronsService.deletePatron(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
}
