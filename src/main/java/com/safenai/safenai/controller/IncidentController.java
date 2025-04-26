package com.safenai.safenai.controller;

import com.safenai.safenai.dto.IncidentRequestDTO;
import com.safenai.safenai.model.Incident;
import com.safenai.safenai.model.User;
import com.safenai.safenai.model.Enums;
import com.safenai.safenai.model.Enums.SeverityLevel;
import com.safenai.safenai.repository.IncidentRepository;
import com.safenai.safenai.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private UserRepository userRepository;

    // Report a new incident
 

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> reportIncident(
            @RequestPart("incident") IncidentRequestDTO incidentRequestDTO,
            @RequestPart(value = "media", required = false) MultipartFile mediaFile) {
    
        // Validate user existence
        User user = userRepository.findById(incidentRequestDTO.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User does not exist");
        }
    
        // Map DTO to Incident entity
        Incident incident = new Incident();
        incident.setUser(user);
        incident.setLatitude(incidentRequestDTO.getLatitude());
        incident.setLongitude(incidentRequestDTO.getLongitude());
        incident.setDescription(incidentRequestDTO.getDescription());
    
        // Handle severity safely
        if (incidentRequestDTO.getSeverity() != null) {
            try {
                incident.setSeverity(Enums.SeverityLevel.valueOf(incidentRequestDTO.getSeverity().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid severity level: " + incidentRequestDTO.getSeverity());
            }
        }
    
        // Handle file saving
        if (mediaFile != null && !mediaFile.isEmpty()) {
            try {
                String uploadDir = "C:/SafenaiUploads"; // Folder where files will be stored
                Files.createDirectories(Paths.get(uploadDir)); // Ensure folder exists
    
                String originalFilename = mediaFile.getOriginalFilename();
                String filename = System.currentTimeMillis() + "_" + originalFilename; // Generate unique filename
                Path filePath = Paths.get(uploadDir).resolve(filename);
    
                Files.copy(mediaFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    
                incident.setImageUrl(filename); // Save only filename or relative path
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to save media: " + e.getMessage());
            }
        }
    
        // Save incident
        Incident savedIncident = incidentRepository.save(incident);
    
        return ResponseEntity.ok(savedIncident);
    }
    

    // Get all incidents for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Incident>> getIncidentsByUser(@PathVariable Long userId) {
        List<Incident> incidents = incidentRepository.findByUserId(userId);
        return ResponseEntity.ok(incidents);
    }

    // Get nearby incidents within a radius
    @GetMapping("/nearby")
    public ResponseEntity<List<Incident>> getNearbyIncidents(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radiusInMeters,
            @RequestParam LocalDateTime startTime) {
        List<Incident> incidents = incidentRepository.findNearbyIncidents(latitude, longitude, radiusInMeters, startTime);
        return ResponseEntity.ok(incidents);
    }

    //get incident by id
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        Incident incident = incidentRepository.getIncidentById(id);
        if (incident == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(incident);
    }

    // Get recent incidents
    @GetMapping("/recent")
    public ResponseEntity<List<Incident>> getRecentIncidents() {
        LocalDateTime now = LocalDateTime.now();
        List<Incident> incidents = incidentRepository.findRecentIncidents(now.toLocalDate().atStartOfDay());
        return ResponseEntity.ok(incidents);
    }

    // Get incident count by day
    @GetMapping("/count-by-day")
    public ResponseEntity<List<Object[]>> getIncidentCountByDay(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<Object[]> counts = incidentRepository.getIncidentCountByDay(startDate, endDate);
        return ResponseEntity.ok(counts);
    }

    // Get recent high-severity incidents
    @GetMapping("/high-severity")
    public ResponseEntity<List<Incident>> getRecentHighSeverityIncidents(@RequestParam LocalDateTime startTime) {
        List<Incident> incidents = incidentRepository.findRecentHighSeverityIncidents(startTime);
        return ResponseEntity.ok(incidents);
    }
}