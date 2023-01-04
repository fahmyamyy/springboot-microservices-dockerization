package com.beyonder.lessonservice.util;

import com.beyonder.lessonservice.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class GetUserDataFromJWT {
    private String JWToken;
    private RestTemplate restTemplate;
    public UserDTO getUserDTO(String token) {
        String newToken = token.split(" ")[1];

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(newToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange("http://localhost:8085/api/v1/auth/user-data-2", HttpMethod.GET, entity, UserDTO.class);

        return UserDTO.builder()
                .id(response.getBody().getId())
                .username(response.getBody().getUsername())
                .password(response.getBody().getPassword())
                .email(response.getBody().getEmail())
                .name(response.getBody().getName())
                .role(response.getBody().getRole())
                .build();
    }
}
