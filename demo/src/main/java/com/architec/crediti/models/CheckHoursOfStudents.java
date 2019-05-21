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
public class CheckHoursOfStudents {
    @Autowired
    AssignmentRepository asRepo ;
    @Autowired
    StudentRepository stRepo;
    @Autowired
    UserRepository usRepo;

    @Autowired
    public CheckHoursOfStudents() {

    }

    @Scheduled(cron = "0 01 15 * * ?")
    public void create() {
        for (Student student : stRepo.findAll()) {
            for ( Assignment as : student.getAssignments()){
                Assignment currentas = asRepo.findByAssignmentId(as.getAssignmentId());
                LocalDate dateline = LocalDate.parse(currentas.getEndDate());
                LocalDate date = LocalDate.now();
                if(dateline.equals(date)){
                    student.setAmoutHours(student.getAmoutHours() + as.getAmountHours());
                    System.out.println(student.getAmoutHours());
                }

            }
        }
    }


}
