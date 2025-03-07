package com.custempmanag.librarymanagmentsystem.service;

import com.custempmanag.librarymanagmentsystem.exception.CustomException;
import com.custempmanag.librarymanagmentsystem.jwt.JwtUtil;
import com.custempmanag.librarymanagmentsystem.models.Users;
import com.custempmanag.librarymanagmentsystem.repository.UserRepository;
import com.custempmanag.librarymanagmentsystem.response.MessageResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,@Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public MessageResponse authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return new MessageResponse(HttpStatus.OK.toString(), "User logged in successfully!", jwtUtil.generateToken(username));
    }

    public MessageResponse registerUser(String username, String password, String role)
    {
        Users users = new Users(username, passwordEncoder.encode(password), role);
        userRepository.save(users);
        return new MessageResponse(HttpStatus.CREATED.toString(), "User registered successfully!", users.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> users = userRepository.findByUsername(username);
        return users.map(u -> User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole().replace("ROLE_", ""))
                .build()
        ).orElseThrow(() -> new CustomException("User not found"));
    }
}
