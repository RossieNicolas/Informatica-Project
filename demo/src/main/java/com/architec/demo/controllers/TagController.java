package com.architec.demo.controllers;

import com.architec.demo.models.Tag;
import com.architec.demo.repositories.TagRepo;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.message.MessageInfo;
import javax.validation.Valid;

@Controller
public class TagController {
    @Autowired
    TagRepo tagRepo;

    @RequestMapping("/tag")
    public String assignment() {
        return "tag";
    }

    @PostMapping("/tag")
    public void createTag(@Valid Tag tag, Model model) {
        boolean added = false;
        for(Tag t : tagRepo.findAll()){
            if(tag.getTagName().equalsIgnoreCase("")){
                added = false;
                model.addAttribute("status", "failed");
            }
            else if (tag.getTagName().equalsIgnoreCase(t.getTagName())) {
                tagRepo.delete(t);
                tagRepo.save(tag);
                added = false;
                model.addAttribute("status", "edit");
            }
            else {
                tagRepo.save(tag);
                added = true;
                model.addAttribute("status", "added");
            }
        }
        if (!added){

        }

    }
}
