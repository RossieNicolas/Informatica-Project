package com.architec.demo.controllers;

import com.architec.demo.models.Student;
import com.architec.demo.models.User;
import com.architec.demo.repositories.UserRepository;
import com.architec.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/createstudentprofile")
    public String getStudentProfileForm() {
        return "createProfileStudent";
    }

}