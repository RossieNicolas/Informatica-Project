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

    @GetMapping("liststudents/{studentennummer}")
    public String getStudentProfile(Principal principal, @PathVariable("studentennummer") String studentennummer, Model model){

        Student st = stuRepo.findByStudentennummer(studentennummer);
        List<File> files = fileRepo.findByUser(st.getUserId());

        model.addAttribute("student", st);
        model.addAttribute("files", files);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "studentDetail";

    }
}

