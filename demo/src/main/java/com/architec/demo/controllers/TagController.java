package com.architec.demo.controllers;

import com.architec.demo.models.Tag;
import com.architec.demo.repositories.TagRepo;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.MessageInfo;
import javax.validation.Valid;
import java.util.List;

@Controller
public class TagController {
    @Autowired
    TagRepo tagRepo;

    //get create tag page
    @RequestMapping("/tag")
    public String assignment() {
        return "tag";
    }

    //make a new tag
    @PostMapping("/tag")
    public String createTag(@Valid Tag tag, Model model) {
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
        return "redirect:/listAllTags";
    }

    //list all tags
    @RequestMapping(value = "/listAllTags", method = RequestMethod.GET)
    public String listAllTags(Model model) /* throws SQLException*/ {

        List<Tag> listAllTags = tagRepo.findAll();
        model.addAttribute("listAllTags", listAllTags);
        return "listAllTags";
    }

    //list specific tag
    @RequestMapping(value = "/listAllTags/{tag_id}", method = RequestMethod.GET)
    public String tags(@PathVariable("tag_id") int tag_id, Model model) {

        List<Tag> tags = tagRepo.findBytagId(tag_id);

        model.addAttribute("tags", tags);
        return "editTag";
    }

    //update specific tag
    @PostMapping(value = "/listAllTags/{tagId}")
    public String saveTag(@PathVariable("tagId") int tagId, @Valid Tag tag) {
        boolean added;
        tag.setTagId(tagId);
        for(Tag t : tagRepo.findAll()){
            if(tag.getTagName().equalsIgnoreCase("")){
                added = false;
            }
            else if (tag.getTagName().equalsIgnoreCase(t.getTagName())) {
                tagRepo.delete(t);
                tagRepo.save(tag);
                added = false;
            }
            else {
                added= true;
                tagRepo.save(tag);
            }
        }
        return "redirect:/listAllTags";
    }

    //delete specific tag
    @GetMapping("/deletetag/{id}")
    public String deleteTag(@PathVariable("id") int id, Model model) {
        Tag tag = tagRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        tagRepo.delete(tag);
        model.addAttribute("tags", tagRepo.findAll());
        return "redirect:/listAllTags";
    }




}
