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
    @Autowired
    TagRepo tagRepo;

    // get create tag page
    @RequestMapping("/tag")
    public String assignment() {
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
    @RequestMapping(value = "/listAllTags", method = RequestMethod.GET)
    public ModelAndView listAllTags(@RequestParam("page") Optional<Integer> page) /* throws SQLException */ {
        ModelAndView modelAndView = new ModelAndView("listAllTags");
        List<Tag> listAllTags = tagRepo.findAll();

        // model.addAttribute("listAllTags", listAllTags);

        int initialPage = 0;
        int pageSize = 15;

        int buttons = (int) tagRepo.count() / pageSize;

        if (tagRepo.count() % pageSize != 0) {
            buttons++;
        }

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
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
    @RequestMapping(value = "/listAllTags/{tag_id}", method = RequestMethod.GET)
    public String tags(@PathVariable("tag_id") int tag_id, Model model) {

        Tag tag = tagRepo.findBytagId(tag_id);

        model.addAttribute("tags", tag);
        return "editTag";
    }

    // update specific tag
    @PostMapping(value = "/listAllTags/{id}")
    public String saveTag(Model model, @PathVariable("id") int id, @Valid Tag tag,
            @RequestParam("tagName") String tagName, @RequestParam("tagDescription") String tagDescription) {
        Tag tagForId = tagRepo.findBytagId(id);
        tag.setTagId(tagForId.getTagId());

        boolean existsName = tagRepo.existsByTagName(tagName);
        if (!existsName) {
            tag.setTagName(tagName);
            tag.setTagDescription(tagDescription);
            tagRepo.save(tag);
        } else {
            model.addAttribute("error", "Tag naam bestaat al!");
            model.addAttribute("tags", tagForId);
            return "editTag";
        }
        return "redirect:/listAllTags";
    }

    // delete specific tag
    @GetMapping("/deletetag/{id}")
    public String deleteTag(@PathVariable("id") int id, Model model) {
        Tag tag = tagRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        tagRepo.delete(tag);
        model.addAttribute("tags", tagRepo.findAll());
        return "redirect:/listAllTags";
    }

}
