package com.tejko.yamb.interfaces.services;

public interface RecaptchaService {
    
    public boolean verifyRecaptcha(String recaptchaToken);

}
