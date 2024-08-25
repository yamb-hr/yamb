package com.tejko.yamb.domain.services;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tejko.yamb.domain.services.interfaces.RecaptchaService;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    @Value("${RECAPTCHA_SECRET_KEY}")
    private String recaptchaSecretKey;

    @Override
    public boolean verifyRecaptcha(String recaptchaToken) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.google.com/recaptcha/api/siteverify")
                .queryParam("secret", recaptchaSecretKey)
                .queryParam("response", recaptchaToken)
                .toUriString();
    
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
    
            return Optional.ofNullable(responseEntity.getBody())
                    .map(body -> (Boolean) body.getOrDefault("success", false))
                    .orElse(false);
    
        } catch (Exception e) {
            System.out.println("Recaptcha verification failed: " + e.getMessage());
            return false;
        }
    }
    
}