package com.architec.crediti.controllers;

import com.architec.crediti.models.ArchivedAssignment;
import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Pager;
import com.architec.crediti.repositories.ArchiveRepository;
import com.architec.crediti.repositories.AssignmentMethods;
import com.architec.crediti.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ArchiveController {
    private final
    ArchiveRepository archiveRepo;
    private int initialPage = 0;
    private int pageSize = 15;
    private final
    TagRepo tagRepo;

    @Autowired
    public ArchiveController(ArchiveRepository archiveRepo, TagRepo tagRepo) {
        this.archiveRepo = archiveRepo;
        this.tagRepo = tagRepo;
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
        modelAndView.addObject("tags", tagRepo.findAll());
        return modelAndView;
    }

    //search in archive
    @PostMapping("/archive")
    String getArchivedAssignment(@RequestParam("searchbar") String name, Model model, @RequestParam("page") Optional<Integer> page,
                                 @RequestParam(required = false , value = "tag") int[] tags) {

        if(tags == null){
            try {
                ArchivedAssignment a = archiveRepo.findByAssignmentId((Integer.parseInt(name)));
                model.addAttribute("assignments", a);

            } catch (Exception e) {
                model.addAttribute("assignments", archiveRepo.findByTitleContaining(name));
            }

        }
        else {
            List<ArchivedAssignment> list = archiveRepo.findByTitleContaining(name);
            List<ArchivedAssignment> list2 = new ArrayList<>();
            for (int item : tags) {
                //TODO
/*                for (ArchivedAssignment a : list) {
                    if (a.getTag_ids().contains(tagRepo.findBytagId(item))) {
                        list2.add(a);
                    }
                }*/
            }

            //delete double assignments in search
            list2 = list2.stream().distinct().collect(Collectors.toList());

            model.addAttribute("assignments", list2);
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
        model.addAttribute("tags", tagRepo.findAll());

        return "archive";
    }
}
