package com.architec.crediti.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "basic/home";
    }

    @GetMapping("/error")
    public  String error() { return "basic/error"; }

}
