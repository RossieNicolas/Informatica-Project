package com.architec.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    // get the login page
    @RequestMapping("/login")
    public String assignment() {
        return "login";
    }
}
