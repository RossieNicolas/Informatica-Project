package com.architec.demo;

import java.util.List;

import com.architec.demo.Repositories.AssignmentRepository;
import com.architec.demo.models.Fiche;

import org.hibernate.annotations.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @Autowired
    private AssignmentRepository conn;

    private List<Fiche> fiches;

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
        fiches = conn.findAll();
        System.out.println(fiches.get(0).getFicheID());

        model.addAttribute("assignments", fiches);

        return "listAllAssignments";
    }

}