package com.tejko.yamb.business.interfaces;

public interface EmailService {

    public void sendSimpleMessage(String to, String subject, String text);
    
}
