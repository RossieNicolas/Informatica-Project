package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Tag;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.TagRepo;
import com.architec.crediti.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AssignmentController {

    private List<Assignment> fiches;
    private List<Assignment> subFiches;

    @Autowired
    TagRepo tagRepo;

    @Autowired
    AssignmentRepository assignmentRepo;

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public String tag(Model model) /* throws SQLException */ {

        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
        return "assignment";
    }

    @PostMapping("/assignment")
    public String createAssignment(@Valid Assignment assignment) {
        User usr = userRepo.findByEmail("s100603@ap.be");
        assignment.setAssignerUserId(usr);
        assignmentRepo.save(assignment);

        return "redirect:/allassignments";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/allassignments")
    public String getAllAssingments(Model model) {
        fiches = assignmentRepo.findAll();
        subFiches = fiches.subList(0, 10);

        model.addAttribute("assignments", subFiches);

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