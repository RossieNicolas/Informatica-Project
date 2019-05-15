package com.architec.crediti.controllers;

import com.architec.crediti.models.ArchivedAssignment;
import com.architec.crediti.models.Pager;
import com.architec.crediti.repositories.ArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ArchiveController {
    private final
    ArchiveRepository archiveRepo;

    private int initialPage = 0;

    private int pageSize = 15;

    @Autowired
    public ArchiveController(ArchiveRepository archiveRepo) {
        this.archiveRepo = archiveRepo;
    }

    //get archive page
    @GetMapping("/archive")
    public ModelAndView showPersonsPage(@RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("archive");

        int buttons = (int) archiveRepo.count() / pageSize;
        if (archiveRepo.count() % pageSize != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? initialPage : page.get() - 1;

        Page<ArchivedAssignment> fiches = archiveRepo.findAll(PageRequest.of(evalPage, pageSize));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        modelAndView.addObject("persons", fiches);
        modelAndView.addObject("assignments", fiches);
        modelAndView.addObject("selectedPageSize", pageSize);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    //search in archive
    @PostMapping("/archive")
    String getArchivedAssignment(@RequestParam("searchbar") String name, Model model, @RequestParam("page") Optional<Integer> page) {

        try {
            ArchivedAssignment a = archiveRepo.findByAssignmentId((Integer.parseInt(name)));
            model.addAttribute("assignments", a);

        } catch (Exception e) {
            model.addAttribute("assignments", archiveRepo.findByTitleContaining(name));
        }

        ModelAndView modelAndView = new ModelAndView("Archive");

        int buttons = (int) archiveRepo.count() / pageSize;
        if (archiveRepo.count() % pageSize != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? initialPage : page.get() - 1;

        Page<ArchivedAssignment> fiches = archiveRepo.findAll(PageRequest.of(evalPage, pageSize));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", pageSize);
        model.addAttribute("pager", pager);

        return "archive";
    }
}
