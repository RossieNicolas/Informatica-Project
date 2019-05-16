package com.architec.crediti.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
