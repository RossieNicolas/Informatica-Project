package com.architec.crediti.controllers;

import com.architec.crediti.models.Pager;
import com.architec.crediti.models.Tag;
import com.architec.crediti.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class TagController {
    private final
    TagRepo tagRepo;

    @Autowired
    public TagController(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    // get create tag page
    @GetMapping("/tag")
    public String tag() {
        return "tag";
    }

    // make a new tag
    @PostMapping("/tag")
    public String createTag(Model model, @RequestParam("tagName") String tagName,
            @RequestParam("tagDescription") String tagDescription) {
        Tag tag = new Tag(tagName, tagDescription);
        boolean existsName = tagRepo.existsByTagName(tagName);
        if (!existsName) {
            tagRepo.save(tag);
        } else {
            model.addAttribute("error", "Tag naam bestaat al!");
            return "tag";
        }
        return "redirect:/listAllTags";
    }

    // list all tags
    @GetMapping("/listAllTags")
    public ModelAndView listAllTags(@RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("listAllTags");

        int initialPage = 0;
        int pageSize = 15;
        int buttons = (int) tagRepo.count() / pageSize;
        if (tagRepo.count() % pageSize != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? initialPage : page.get() - 1;

        Page<Tag> fiches = tagRepo.findAll(PageRequest.of(evalPage, pageSize));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        modelAndView.addObject("tags", fiches);
        modelAndView.addObject("listAllTags", fiches);
        modelAndView.addObject("selectedPageSize", pageSize);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    // list specific tag
    @GetMapping(value = {"/editTag", "/listAllTags/{tag_id}"})
    public String tags(@PathVariable("tag_id") int tagId, Model model) {

        Tag tag = tagRepo.findBytagId(tagId);

        model.addAttribute("tags", tag);
        return "editTag";
    }

    // update specific tag
    @PostMapping(value = "/listAllTags/{id}")
    public String saveTag(Model model, @PathVariable("id") int id, @Valid Tag tag, @RequestParam("tagName") String tagName) {
        Tag tagForId = tagRepo.findBytagId(id);
        boolean existsName = tagRepo.existsByTagName(tagName);

        if(tagForId!= null && (!existsName || tagForId.getTagName().equals(tag.getTagName())))  {
            tagForId.setTagName(tag.getTagName());
            tagForId.setTagDescription(tag.getTagDescription());
            tagRepo.save(tagForId);
        } else{
            model.addAttribute("error", "Tag naam bestaat al!");
            model.addAttribute("tags", tagForId);
            return "editTag";
        }
        return "redirect:/listAllTags";
    }

    // delete specific tag
    @GetMapping("/deletetag/{id}")
    public String deleteTag(@PathVariable("id") int id, Model model) {
        try{
            Tag tag = tagRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
            tagRepo.delete(tag);

            model.addAttribute("tags", tagRepo.findAll());
            return "redirect:/listAllTags";

        }catch (Exception ex){
            return "tagStillAssigned";
        }

    }

}
