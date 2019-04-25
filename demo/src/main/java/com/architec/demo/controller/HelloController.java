package com.architec.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {

    private JDBCcontroller connectie;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/allassignments")
    public String getAllAssingments(@RequestParam(name = "input", required = false) String input, Model model) {
        connectie = new JDBCcontroller();
        input = connectie.findAll();
        model.addAttribute("name", input);
        return "listAllAssignments";
    }

}