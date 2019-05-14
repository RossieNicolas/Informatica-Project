package com.architec.crediti.controllers;

import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private final
    ExternalUserRepository exRepo;

    private final
    UserRepository userRepo;

    @Autowired
    public LoginController(ExternalUserRepository exRepo, UserRepository userRepo) {
        this.exRepo = exRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/loginError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }
}
