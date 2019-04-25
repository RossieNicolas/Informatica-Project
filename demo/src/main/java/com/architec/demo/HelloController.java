package com.architec.demo;

import java.util.List;

import com.architec.demo.Repositories.AssignmentRepository;
import com.architec.demo.Repositories.UserRepository;
import com.architec.demo.models.Assignment;
import com.architec.demo.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @Autowired
    private AssignmentRepository AssignmentRepo;

    @Autowired
    private UserRepository userRepo;

    private List<Assignment> fiches;
    private List<User> users;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/allassignments")
    public String getAllAssingments(Model model) {
        fiches = AssignmentRepo.findAll();
        users = userRepo.findAll();

        model.addAttribute("assignments", fiches);
        model.addAttribute("user", users);

        return "listAllAssignments";
    }

}