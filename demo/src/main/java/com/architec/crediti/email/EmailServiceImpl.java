package com.architec.crediti.email;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    private Log log = LogFactory.getLog(this.getClass());

    private final UserRepository userRepo;

    public EmailServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);

            List<User> coordinators = userRepo.findAllByRole(Role.COORDINATOR);
            for(User u: coordinators){
                message.setCc(u.getEmail());
            }

            message.setCc();
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException ex) {
            log.error(ex.toString());
        }
    }
}
