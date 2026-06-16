package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public String register(
            @Valid @RequestBody RegisterRequest request) {

        if (userRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setName(request.getName());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(request.getRole().toUpperCase());

        userRepository.save(user);

        return "User Registered Successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByName(request.getName())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String token =
                jwtService.generateToken(
                        user.getName(),
                        user.getRole()
                );

        return new AuthResponse(token);
    }
}