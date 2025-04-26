package com.safenai.safenai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncidentRequestDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotBlank(message = "Description is required")
    private String description;
    // Optional field for severity, if not provided, default will be used
    private String severity = "LOW"; // Default severity level
    // Optional field for category, if not provided, default will be used
}