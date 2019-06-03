package com.architec.crediti.controllers;

import com.architec.crediti.models.File;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.FileRepository;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    private final
    StudentRepository stuRepo;
    private final UserRepository userRepo;


    private final
    FileRepository fileRepo;

    @Autowired
    public ProfileController(StudentRepository stuRepo , FileRepository fileRepo, UserRepository userRepo) {
        this.stuRepo = stuRepo;
        this.fileRepo = fileRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("liststudents/{studentnumber}")
    public String getStudentProfile(Principal principal, @PathVariable("studentnumber") String studentNumber, Model model){

        Student st = stuRepo.findByStudentNumber(studentNumber);
        List<File> files = fileRepo.findByUserOrderByAssignmentId(st.getUserId());

        model.addAttribute("student", st);
        model.addAttribute("files", files);
        model.addAttribute("status", fileRepo.findByDocType("Contract"));
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "student/studentDetail";
    }
}

