package io.github.chriszht.weatherapp.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ApiClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static <T> T get(String url, Class<T> responseType) {
        ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);
        return response.getBody();
    }
}