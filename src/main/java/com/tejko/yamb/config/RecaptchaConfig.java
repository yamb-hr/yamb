package com.tejko.yamb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "recaptcha.key")
public class RecaptchaConfig {

    private String site;
    private String secret;

    public String getSite() {
        return site;
    }
    
    public void setSite(String site) {
        this.site = site;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
}