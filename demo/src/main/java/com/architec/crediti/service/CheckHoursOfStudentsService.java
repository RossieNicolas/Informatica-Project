package com.architec.crediti.service;

import java.time.LocalDate;

import javax.transaction.Transactional;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Student;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Transactional
@Component
public class CheckHoursOfStudentsService {
    private final
    AssignmentRepository asRepo ;
    private final
    StudentRepository stRepo;
    final
    UserRepository usRepo;

    @Autowired
    public CheckHoursOfStudentsService(AssignmentRepository asRepo, StudentRepository stRepo, UserRepository usRepo) {

        this.asRepo = asRepo;
        this.stRepo = stRepo;
        this.usRepo = usRepo;
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
