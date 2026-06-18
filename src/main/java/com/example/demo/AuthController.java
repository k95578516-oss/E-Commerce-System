package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // SIGNUP
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole("USER");
        }

        userRepository.save(user);

        return "User registered successfully";
    }

    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Optional<User> dbUser = userRepository.findByName(user.getName());

        if (dbUser.isPresent() &&
                passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {

            return jwtService.generateToken(
                    dbUser.get().getName(),
                    dbUser.get().getRole()
            );
        }

        return "Invalid credentials";
    }
}