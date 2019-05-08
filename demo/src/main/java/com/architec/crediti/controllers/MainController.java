package com.architec.crediti.controllers;

import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    UserRepository userRepo;

    @RequestMapping("main")
    public String getDashboard(Principal principal) {
        String name = principal.getName();
        System.out.println(name);
        return "main";
    }
}
