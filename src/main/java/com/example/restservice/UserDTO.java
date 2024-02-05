package com.example.restservice;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String fullName;
}
