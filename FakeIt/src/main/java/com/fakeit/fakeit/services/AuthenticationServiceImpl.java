package com.fakeit.fakeit.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

@Service
public class AuthenticationServiceImpl  implements AuthenticationService {
    private static final String FIREBASE_LOGIN_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=";
    private static final String FIREBASE_REGISTER_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=";

    private String getApiKeyFromFile() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(getClass().getClassLoader().getResource("firebaseApiKey.json"));
        return jsonNode.get("key").asText();
    }

    @Override
    public String authWithFirebase(String email, String password) {
        String apiKey;
        try {
            apiKey = getApiKeyFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return getToken(email, password, FIREBASE_LOGIN_URL + apiKey);
    }

    @Override
    public String register(String email, String password, String username) {
        String apiKey;
        try {
            apiKey = getApiKeyFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return getToken(email, password, FIREBASE_REGISTER_URL + apiKey);
    }
    private String getToken(String email, String password, String url) {
        String requestBody = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonNode jsonResponse = new ObjectMapper().readTree(response.getBody());
                return jsonResponse.get("idToken").textValue();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }
        return "Error con la autenticacion";
    }
}
