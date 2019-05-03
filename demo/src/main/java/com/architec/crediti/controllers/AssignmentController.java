package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Page;
import com.architec.crediti.models.Tag;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.TagRepo;
import com.architec.crediti.service.PageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AssignmentController {

    private List<Assignment> fiches;

    @Autowired
    TagRepo tagRepo;

    @Autowired
    AssignmentRepository assignmentRepo;

    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public String tag(Model model) /* throws SQLException */ {

        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
        return "assignment";
    }

    @PostMapping("/assignment")
    public Assignment createAssignment(@Valid Assignment assignment) {
        return assignmentRepo.save(assignment);
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/allassignments")
    public String getAllAssingments(Model model) {
        final int currentPage = 1;
        final int pageSize = 20;

        fiches = assignmentRepo.findAll();
        model.addAttribute("assignments", fiches);

        Page<Assignment> assignmentPage = PageService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("assPage", assignmentPage);

        int totalPages = assignmentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "listAllAssignments";
    }

    @RequestMapping(value = "/myassignments", method = RequestMethod.GET)
    public String assignments(Model model) {
        List<Assignment> assignments = assignmentRepo.findAll();

        model.addAttribute("assignments", assignments);

        return "myassignments";
    }

    @GetMapping("/unvalidatedassignments")
    public String getUnvalidatedAssingments(Model model) {
        fiches = assignmentRepo.findAll();
        List<Assignment> unvalidatedFiches = new ArrayList<>();

        for (Assignment item : fiches) {
            if (!item.isValidated()) {
                unvalidatedFiches.add(item);
            }
        }

        model.addAttribute("assignments", unvalidatedFiches);

        return "listUnvalidatedAssignments";
    }

    @GetMapping("/detailfiche")
    public String getDetailFiche(Model model) {
        fiches = assignmentRepo.findAll();

        Assignment output = new Assignment();
        for (Assignment item : fiches) {
            if (item.getAssignmentId() == 81) {
                output = item;
            }
        }

        model.addAttribute("assignments", output);

        return "detailFiche";
    }

    @PostMapping("/goedkeuren")
    public String ficheGoedkeuren() {
        return "";
    }
}