package com.architec.crediti.controllers;

import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.Methods;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    StudentRepository stuRepo;

    @GetMapping("liststudents/{studentennummer}")
    public String getStudentProfile(Principal principal, @PathVariable("studentennummer") int studentennummer, Model model){
        if(Methods.isCoordinator(userRepo.findByEmail(principal.getName())) || Methods.isLector(userRepo.findByEmail(principal.getName())) ){
        Student st = stuRepo.findByStudentennummer(studentennummer);
        model.addAttribute("student", st);
        return "studentDetail";
        }
        return "main";
    }
}
