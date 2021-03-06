package com.architec.crediti.controllers;

import com.architec.crediti.models.*;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.FileRepository;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    private final
    StudentRepository stuRepo;

    private final
    UserRepository userRepo;

    private final
    AssignmentRepository assignmentRepo;


    private final
    FileRepository fileRepo;

    @Autowired
    public ProfileController(StudentRepository stuRepo, FileRepository fileRepo, UserRepository userRepo, AssignmentRepository assRepo) {
        this.stuRepo = stuRepo;
        this.fileRepo = fileRepo;
        this.userRepo = userRepo;
        this.assignmentRepo = assRepo;
    }

    @GetMapping("liststudents/{studentnumber}")
    public String getStudentProfile(Principal principal, @PathVariable("studentnumber") String studentNumber, Model model) {

        Student st = stuRepo.findByStudentNumber(studentNumber);
        User us = userRepo.findByUserId(st.getUserId().getUserId());
        List<File> files = fileRepo.findByUserOrderByAssignmentId(us);
        List<File> contracts = fileRepo.findByDocTypeAndUser("Contract", us);

        model.addAttribute("student", st);
        model.addAttribute("files", files);
        model.addAttribute("status", contracts);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "student/studentDetail";
    }

    @GetMapping("/changestatus/{studentId}/{assignmentId}")
    public String changeStatus(@PathVariable("studentId") String userId, @PathVariable("assignmentId") int assignmentId) {
        Student usr = stuRepo.findByStudentNumber(userId);
        User u = userRepo.findByUserId(usr.getUserId().getUserId());

        List<File> files = fileRepo.findByUser(u);
        List<File> rightFiles = new ArrayList<>();
        File file = null;

        for (File f : files) {
            if (f.getAssignmentId() == assignmentId) {
                rightFiles.add(f);

                if (f.getDocType().equalsIgnoreCase("Contract")) {
                    file = f;
                }
            }
        }

        Progress status;
        status = file.getStatus();
        Progress newStatus;
        if (status.equals(Progress.START)) {
            newStatus = Progress.CONTRACT;
        } else if (status.equals(Progress.CONTRACT)) {
            newStatus = Progress.PORTFOLIO;
        } else if (status.equals(Progress.PORTFOLIO)) {
            newStatus = Progress.BEWIJS;
        } else if (status.equals(Progress.BEWIJS)) {
            newStatus = Progress.END;
            Assignment ass = assignmentRepo.findByAssignmentId(file.getAssignmentId());
            usr.setAmoutHours(usr.getAmoutHours() + ass.getAmountHours());
        } else {
            newStatus = Progress.END;
        }

        for (File f : rightFiles) {
            f.setStatus(newStatus);
        }

        fileRepo.save(file);
        stuRepo.save(usr);
        userRepo.save(u);

        return "redirect:/liststudents/" + userId + "#Status";
    }

}

