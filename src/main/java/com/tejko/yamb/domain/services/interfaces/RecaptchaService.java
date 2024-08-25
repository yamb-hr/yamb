package com.tejko.yamb.domain.services.interfaces;

public interface RecaptchaService {
    
    public boolean verifyRecaptcha(String recaptchaToken);

}
