package com.example.userservice.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String role;
}