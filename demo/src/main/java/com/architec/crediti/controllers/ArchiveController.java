package com.architec.crediti.controllers;

import com.architec.crediti.models.*;
import com.architec.crediti.repositories.ArchiveRepository;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.TagRepo;
import com.architec.crediti.repositories.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ArchiveController {
    private final
    ArchiveRepository archiveRepo;
    private static final int PAGE_SIZE = 15;
    private static final int INITAL_PAGE = 0;
    private Log log = LogFactory.getLog(this.getClass());
    private final
    TagRepo tagRepo;
    private final UserRepository userRepo;
    private final AssignmentRepository assignmentRepo;

    @Autowired
    public ArchiveController(ArchiveRepository archiveRepo, AssignmentRepository assignmentRepo, TagRepo tagRepo, UserRepository userRepo) {
        this.archiveRepo = archiveRepo;
        this.assignmentRepo = assignmentRepo;
        this.tagRepo = tagRepo;
        this.userRepo = userRepo;
    }

    //get archive page
    @GetMapping("/archive")
    public ModelAndView showPersonsPage(@RequestParam("page") Optional<Integer> page, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("archive/archive");

        int buttons = (int) archiveRepo.count() / PAGE_SIZE;
        if (archiveRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        List<Tag> allTags = tagRepo.findAll();

        boolean[] status = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            status[i] = false;

        }

        Page<ArchivedAssignment> fiches = archiveRepo.findAllByOrderByAssignmentIdDesc(PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        User currentUser = userRepo.findByEmail(principal.getName());
        modelAndView.addObject("status", status);
        modelAndView.addObject("persons", fiches);
        modelAndView.addObject("assignments", fiches);
        modelAndView.addObject("selectedPageSize", PAGE_SIZE);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("tags", tagRepo.findAll());
        modelAndView.addObject("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return modelAndView;
    }

    //search in archive
    @GetMapping("/archive/{searchbar}")
    public String searchByTitleOrId(@PathVariable("searchbar") String name, Model model, @RequestParam("page") Optional<Integer> page, Principal principal) {
        Page fiches = null;

        int buttons = (int) archiveRepo.count() / PAGE_SIZE;
        if (archiveRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;


        try {
            fiches = archiveRepo.findByAssignmentIdOrderByAssignmentIdDesc((Integer.parseInt(name)), PageRequest.of(evalPage, PAGE_SIZE));

        } catch (Exception e) {
            fiches = archiveRepo.findByTitleContainingOrTagNameContainingOrderByAssignmentIdDesc(name, name, PageRequest.of(evalPage, PAGE_SIZE));

        }

        List<Tag> allTags = tagRepo.findAll();

        boolean[] status = new boolean[allTags.size()];

        for (int i = 0; i < allTags.size(); i++) {
            status[i] = false;

        }

        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("status", status);
        model.addAttribute("assignments", fiches);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "archive/archive";
    }
}

