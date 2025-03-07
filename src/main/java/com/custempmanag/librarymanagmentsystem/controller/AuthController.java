package com.custempmanag.librarymanagmentsystem.controller;

import com.custempmanag.librarymanagmentsystem.models.Users;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import com.custempmanag.librarymanagmentsystem.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        MessageResponse messageResponse = authService.authenticate(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        MessageResponse messageResponse = authService.registerUser(user.getUsername(), user.getPassword(), user.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }
}

