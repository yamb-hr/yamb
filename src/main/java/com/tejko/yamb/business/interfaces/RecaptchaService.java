package com.tejko.yamb.business.interfaces;

public interface RecaptchaService {
    
    public boolean verifyRecaptcha(String recaptchaToken);

}
