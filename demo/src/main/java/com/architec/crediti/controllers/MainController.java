package com.architec.crediti.controllers;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    private final
    UserRepository userRepo;

    private final
    ExternalUserRepository exRepo;

    @Autowired
    public MainController(UserRepository userRepo, ExternalUserRepository exRepo) {
        this.userRepo = userRepo;
        this.exRepo = exRepo;
    }

    @GetMapping("/main")
    public String getDashboard(Principal principal, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());

        if (currentUser.isFirstLogin()) {
            return "redirect:/createstudentprofile";
        }

        if (exRepo.findByUserId(currentUser) != null && !exRepo.findByUserId(currentUser).isApproved()) {
            return "redirect:/notapproved";

        }
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "/basic/main";
    }
}
