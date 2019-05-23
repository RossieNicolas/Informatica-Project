package com.architec.crediti.service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Student;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Transactional
@Component
public class CheckAssignmentDeadlineService {
    private final
    AssignmentRepository asRepo;

    private final
    StudentRepository stRepo;

    private final EmailServiceImpl mail;

    @Autowired
    public CheckAssignmentDeadlineService(EmailServiceImpl mail, AssignmentRepository asRepo, StudentRepository stRepo) {
        this.mail = mail;
        this.asRepo = asRepo;
        this.stRepo = stRepo;
    }

    @Scheduled(cron = "0 00 05 * * ?")
    public void create() {
         for (Student student : stRepo.findAll()) {
             for ( Assignment as : student.getAssignments()){
                Assignment currentas = asRepo.findByAssignmentId(as.getAssignmentId());
                LocalDate dateline = LocalDate.parse(currentas.getEndDate());
                LocalDate date = LocalDate.now().plusDays(1);
                 if(dateline.equals(date)){
                    mail.sendSimpleMessage(student.getEmail(), "Herinnering",
                    EmailTemplates.reminder(currentas.getTitle()));
                 }

            }
        }
    }
}
