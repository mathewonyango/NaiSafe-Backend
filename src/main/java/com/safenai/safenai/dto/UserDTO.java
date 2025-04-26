package com.safenai.safenai.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String googleId;
    private String facebookId;
}