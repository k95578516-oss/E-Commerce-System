package com.example.demo;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // SIGNUP
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {

        if (userRepository.findByName(user.getName()).isPresent()) {
            return "Username already exists";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole("USER");
        }

        userRepository.save(user);

        return "User registered successfully";
    }

    // LOGIN (FIXED)
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User dbUser = userRepository.findByName(request.getName())
                .orElse(null);

        if (dbUser == null) {
            return "Invalid credentials";
        }

        if (!passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {
            return "Invalid credentials";
        }

        return jwtService.generateToken(
                dbUser.getName(),
                dbUser.getRole()
        );
    }
}