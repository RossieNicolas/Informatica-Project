package com.architec.crediti.controllers;

import com.architec.crediti.models.ArchivedAssignment;
import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Pager;
import com.architec.crediti.repositories.ArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class ArchiveController {
    @Autowired
    ArchiveRepository archiveRepo;
    @GetMapping("/archive")
    public ModelAndView showPersonsPage(@RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("archive");
        int initialPage = 0;
        int pageSize = 15;

        int buttons = (int) archiveRepo.count() / pageSize;

        if (archiveRepo.count() % pageSize != 0) {
            buttons++;
        }

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? initialPage : page.get() - 1;

        Page<ArchivedAssignment> fiches = archiveRepo.findAll(PageRequest.of(evalPage, pageSize));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        modelAndView.addObject("persons", fiches);
        modelAndView.addObject("assignments", fiches);
        modelAndView.addObject("selectedPageSize", pageSize);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }
}
