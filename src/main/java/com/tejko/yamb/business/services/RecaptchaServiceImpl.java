package com.tejko.yamb.business.services;

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

import com.tejko.yamb.business.interfaces.RecaptchaService;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    private static final Logger logger = LoggerFactory.getLogger(RecaptchaServiceImpl.class);

    @Value("${RECAPTCHA_SECRET_KEY}")
    private String recaptchaSecretKey;

    @Override
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