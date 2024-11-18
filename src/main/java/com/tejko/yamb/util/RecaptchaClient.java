package com.tejko.yamb.util;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RecaptchaClient {

    private static final Logger logger = LoggerFactory.getLogger(RecaptchaClient.class);

    @Value("${recaptcha.secret.key}")
    private String recaptchaSecretKey;

    public boolean verifyRecaptcha(String recaptchaToken) {
        String url = buildRecaptchaVerificationUrl(recaptchaToken);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            return extractSuccessFromResponse(responseEntity);
        } catch (Exception e) {
            logger.info("Failed to verify recaptcha: " + e.getMessage());
            return false;
        }
    }

    private String buildRecaptchaVerificationUrl(String recaptchaToken) {
        return UriComponentsBuilder.fromHttpUrl("https://www.google.com/recaptcha/api/siteverify")
                .queryParam("secret", recaptchaSecretKey)
                .queryParam("response", recaptchaToken)
                .toUriString();
    }

    private boolean extractSuccessFromResponse(ResponseEntity<Map<String, Object>> responseEntity) {
        return Optional.ofNullable(responseEntity.getBody())
                .map(body -> (Boolean) body.getOrDefault("success", false))
                .orElse(false);
    }
    
}