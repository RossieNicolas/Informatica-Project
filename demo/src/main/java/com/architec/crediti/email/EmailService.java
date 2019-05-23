package com.architec.crediti.email;

public interface EmailService {
    void sendSimpleMessage(String to,
                           String subject,
                           String text);

}
