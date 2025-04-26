package com.safenai.safenai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safenai.safenai.model.Enums.SeverityLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Prevents serialization of the user field
 // Marks this side as the child
    private User user;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank
    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private SeverityLevel severity;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private boolean verified = false;
    
    private String imageUrl;
    
    private String category;
    
    @Column(length = 1000)
    private String aiAnalysis;
    
    private Integer upvotes = 0;
    private Integer downvotes = 0;
}