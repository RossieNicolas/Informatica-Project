package com.architec.demo.controllers;

import com.architec.demo.models.Tag;
import com.architec.demo.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.sql.Connection;
import java.sql.DriverManager;

@Controller
public class TagController {
    @Autowired
    TagRepo tagRepo;
    @RequestMapping("/tag")
    public String assignment() {
        return "tag";
    }

    @PostMapping("/tag")
    public Tag createTag(@Valid Tag tag){
        return tagRepo.save(tag);
    }
}
