package com.architec.crediti.controllers;

import com.architec.crediti.models.*;
import com.architec.crediti.repositories.ArchiveRepository;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.TagRepo;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ArchiveController {
    private final
    ArchiveRepository archiveRepo;
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 15;
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
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<ArchivedAssignment> fiches = archiveRepo.findAll(PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        User currentUser = userRepo.findByEmail(principal.getName());
        modelAndView.addObject("persons", fiches);
        modelAndView.addObject("assignments", fiches);
        modelAndView.addObject("selectedPageSize", PAGE_SIZE);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("tags", tagRepo.findAll());
        modelAndView.addObject("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return modelAndView;
    }

    //search in archive
    @PostMapping("/archive")
    public String getArchivedAssignment(@RequestParam("searchbar") String name, Model model, @RequestParam("page") Optional<Integer> page,
                                        @RequestParam(required = false, value = "tag") int[] tags) {

        if (tags == null) {
            try {
                ArchivedAssignment a = archiveRepo.findByAssignmentId((Integer.parseInt(name)));
                model.addAttribute("assignments", a);

            } catch (Exception e) {
                model.addAttribute("assignments", archiveRepo.findByTitleContaining(name));
            }

        } else {
            List<ArchivedAssignment> list = archiveRepo.findByTitleContaining(name);
            List<ArchivedAssignment> list2 = new ArrayList<>();
            for (int item : tags) {
                for (ArchivedAssignment a : list) {
                    String archiveTag = tagRepo.findBytagId(item).getTagName();
                    String tag = a.getTagName();
                    List<String> items = Arrays.asList(tag.replace("[", "").replace("]", "").split("\\s*,\\s*"));

                    for (int i = 0; i < items.size(); i++) {
                        if (archiveTag.contains(items.get(i)) && !tag.equals("[]")) {
                            list2.add(a);
                        }
                    }

                }
            }

            //delete double assignments in search
            list2 = list2.stream().distinct().collect(Collectors.toList());

            model.addAttribute("assignments", list2);
        }

        int buttons = (int) archiveRepo.count() / PAGE_SIZE;
        if (archiveRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<ArchivedAssignment> fiches = archiveRepo.findAll(PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());

        return "archive/archive";
    }

}
