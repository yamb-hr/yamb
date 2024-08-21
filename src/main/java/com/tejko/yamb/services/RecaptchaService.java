package com.tejko.yamb.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RecaptchaService {

    @Value("${RECAPTCHA_SECRET_KEY}")
    private String recaptchaSecretKey;

    public boolean verifyRecaptcha(String recaptchaToken) {
        String url = UriComponentsBuilder.fromHttpUrl("https://www.google.com/recaptcha/api/siteverify")
                .queryParam("secret", recaptchaSecretKey)
                .queryParam("response", recaptchaToken)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
        System.out.println("Recaptcha response: " + response);
        return (Boolean) response.getOrDefault("success", false);
    }
}