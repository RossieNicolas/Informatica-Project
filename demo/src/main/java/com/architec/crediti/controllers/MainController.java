package com.architec.crediti.controllers;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    ExternalUserRepository exRepo;

    @RequestMapping("/main")
    public String getDashboard(Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());

        if (currentUser.isFirstLogin()) {
            return "redirect:createstudentprofile";
        }

        if (exRepo.findByUserId(currentUser) != null) {
            if (!exRepo.findByUserId(currentUser).isApproved()) {
                return "redirect:notapproved";
            }
        }

        return "main";
    }
}
