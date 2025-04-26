package com.safenai.safenai.controller;

import com.safenai.safenai.dto.LoginRequest;
import com.safenai.safenai.dto.RegisterRequest;
import com.safenai.safenai.model.Enums.UserRole;
import com.safenai.safenai.model.User;
import com.safenai.safenai.repository.UserRepository;
import com.safenai.safenai.security.JwtService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

   @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    System.out.println("Registering user: " + request.getEmail());
    
    // Check if the email already exists
    if (userRepo.existsByEmail(request.getEmail())) {
        return ResponseEntity.badRequest().body("User already exists");
    }

    // Create a new user with builder pattern or create a new user and set only required fields
    User user = new User();
    user.setEmail(request.getEmail());
    user.setName(request.getName());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    // Set default values as needed
    user.setRole(UserRole.ROLE_USER); // Assuming you have an enum for roles
    user.setEmailNotificationsEnabled(true); // Default to enabled
    user.setPushNotificationsEnabled(true);
    user.setAlertRadius(5); // Default alert radius in km
    
    // Save the user
    userRepo.save(user);
    String token = jwtService.generateToken(user);
    Map<String, Object> userData = new HashMap<>();
    userData.put("email", user.getEmail());
    userData.put("name", user.getName());
    userData.put("token", token);
    
    // Generate and return the JWT token
   
    return ResponseEntity.ok().body(userData);
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

    System.out.println("Received email from app: " + request.getEmail());
    System.out.println("Received password from app: " + request.getPassword());
        User user = userRepo.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
 // Create a response body with email, name, and token
    Map<String, Object> userData = new HashMap<>();
    userData.put("email", user.getEmail());
    userData.put("name", user.getName());
    userData.put("token", token);

    return ResponseEntity.ok().body(userData);    }
}