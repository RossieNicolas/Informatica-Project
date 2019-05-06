package com.architec.crediti.controllers;

import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

    @Autowired
    UserRepository userRepo;

    @RequestMapping("dashboard")
    public String getDashboard() {

        return "dashboard";
    }
}
