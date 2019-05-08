package com.architec.crediti.controllers;

import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class StudentController {

    @Autowired
    UserRepository userRepo;

    @GetMapping("/createstudentprofile")
    public String getStudentProfileForm() {
        return "createProfileStudent";
    }

    @PostMapping("/createstudentprofile")
    public String createUser(Principal principal, @RequestParam("gsm") String gsm, @RequestParam("class") String s_class) {

        User currentUser = userRepo.findUserByEmail(principal.getName());

        if (currentUser.isFirstLogin()) {
            currentUser.setGsm(gsm);
            currentUser.setZapOrMobility(s_class);
            currentUser.setFirstLogin(false);
            userRepo.save(currentUser);
        }

        return "redirect:/registersucces";
    }

}
