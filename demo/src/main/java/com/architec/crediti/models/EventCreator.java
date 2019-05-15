package com.architec.crediti.models;

import java.time.LocalDate;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.bytebuddy.asm.Advice.Local;


@Component
public class EventCreator {
    AssignmentRepository asRepo ; 
    StudentRepository stRepo;
    private final EmailServiceImpl mail;
    public EventCreator(EmailServiceImpl mail) {
        this.mail = mail;
    }
    @Scheduled(cron = "0 56 12 * * ?")
    public void create() {
            for (Student student : stRepo.findAll()) {
                for ( Assignment as : student.getAssignments(){
                    Assignment currentas = asRepo.findByAssignmentId(as.getAssignmentId());
                    LocalDate aLD = LocalDate.parse(currentas.getEnd_date());
                    if(aLD == LocalData.Today().minusDays(1)){
                        for (type var : iterable) {
                            
                        }
                    }
                
                }
            }
    

                // mail.sendSimpleMessage("alina.storme@student.ap.be", "testing",
                // EmailTemplates.reminder());
    }
}
