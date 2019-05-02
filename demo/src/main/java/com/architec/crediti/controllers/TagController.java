package com.architec.crediti.controllers;

import com.architec.crediti.models.Tag;
import com.architec.crediti.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class TagController {
    @Autowired
    TagRepo tagRepo;

    // get the form to make a tag
    @RequestMapping("/tag")
    public String assignment() {
        return "tag";
    }

    // save the tag in the database
    @PostMapping("/tag")
    public Tag createTag(@Valid Tag tag) {
        return tagRepo.save(tag);
    }
}
