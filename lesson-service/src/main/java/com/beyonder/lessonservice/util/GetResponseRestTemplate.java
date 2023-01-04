package com.beyonder.lessonservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class GetResponseRestTemplate<T> {
    private String token;
    private Class<T> paramClass;
    private RestTemplate restTemplate;
    public ResponseEntity<T> getResponse(Class<T> paramClass, String url, String token){
        String newToken = token.split(" ")[1];
        log.info(newToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(newToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, paramClass);
    }

}

