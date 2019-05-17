package com.architec.crediti.models;

import java.time.LocalDate;

import javax.transaction.Transactional;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.bytebuddy.asm.Advice.Local;

@Transactional
@Component
public class CheckAssignmentDateline {
    @Autowired
    AssignmentRepository asRepo ; 
    @Autowired
    StudentRepository stRepo;
    @Autowired
    UserRepository usRepo;
    private final EmailServiceImpl mail;
    public CheckAssignmentDateline(EmailServiceImpl mail) {
        this.mail = mail;
    }
    @Scheduled(cron = "0 00 05 * * ?")
    public void create() {
             for (Student student : stRepo.findAll()) {
                 for ( Assignment as : student.getAssignments()){
                    Assignment currentas = asRepo.findByAssignmentId(as.getAssignmentId());
                    LocalDate dateline = LocalDate.parse(currentas.getEnd_date());
                    LocalDate date = LocalDate.now().plusDays(1);
                     if(dateline.equals(date)){
                        mail.sendSimpleMessage(student.getEmail(), "Herinnering",
                        EmailTemplates.reminder(currentas.getTitle()));
                     }
                
                }
            }
        }
        

    }
        

                
    
