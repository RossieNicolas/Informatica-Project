package com.architec.crediti.controllers;

import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @Autowired
    ExternalUserRepository exRepo;

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/login")
    public String getLogin() {
        return "login";
    }

    @RequestMapping("/loginError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }
}
