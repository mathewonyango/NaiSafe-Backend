package com.safenai.safenai.controller;

import com.safenai.safenai.dto.UserDTO;
import com.safenai.safenai.model.User;
import com.safenai.safenai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Get all users
   @GetMapping("/all")
public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<User> users = userRepository.findAll();
    List<UserDTO> userDTOs = users.stream()
            .map(user -> {
                UserDTO dto = new UserDTO();
                dto.setId(user.getId());
                dto.setName(user.getName());
                dto.setEmail(user.getEmail());
                dto.setGoogleId(user.getGoogleId());
                dto.setFacebookId(user.getFacebookId());
                return dto;
            })
            .collect(Collectors.toList());
    return ResponseEntity.ok(userDTOs);
}

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> {
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setGoogleId(u.getGoogleId());
            dto.setFacebookId(u.getFacebookId());
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Get user by email
    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> {
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setGoogleId(u.getGoogleId());
            dto.setFacebookId(u.getFacebookId());
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Get users within alert radius
    @GetMapping("/alert-radius")
    public ResponseEntity<List<UserDTO>> getUsersInAlertRadius(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        List<User> users = userRepository.findUsersInAlertRadius(latitude, longitude);
        List<UserDTO> userDTOs = users.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    dto.setGoogleId(user.getGoogleId());
                    dto.setFacebookId(user.getFacebookId());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // Get user by Google ID
    @GetMapping("/google/{googleId}")
    public ResponseEntity<?> getUserByGoogleId(@PathVariable String googleId) {
        Optional<User> user = userRepository.findByGoogleId(googleId);
        return user.map(u -> {
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setGoogleId(u.getGoogleId());
            dto.setFacebookId(u.getFacebookId());
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Get user by Facebook ID
    @GetMapping("/facebook/{facebookId}")
    public ResponseEntity<?> getUserByFacebookId(@PathVariable String facebookId) {
        Optional<User> user = userRepository.findByFacebookId(facebookId);
        return user.map(u -> {
            UserDTO dto = new UserDTO();
            dto.setId(u.getId());
            dto.setName(u.getName());
            dto.setEmail(u.getEmail());
            dto.setGoogleId(u.getGoogleId());
            dto.setFacebookId(u.getFacebookId());
            return ResponseEntity.ok(dto);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Check if email exists
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfEmailExists(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}