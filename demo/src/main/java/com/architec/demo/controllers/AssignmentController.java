package com.architec.demo.controllers;

import com.architec.demo.models.Assignment;
import com.architec.demo.models.Tag;
import com.architec.demo.repositories.AssignmentRepository;
import com.architec.demo.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    TagRepo tagRepo;

    @Autowired
    AssignmentRepository assignmentRepo;

    @RequestMapping("/assignment")
    public String assignment() {
        return "assignment";
    }

    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public String tag(Model model) /*throws SQLException*/ {

        List<Tag> updatetag = tagRepo.findAll();


        model.addAttribute("updatetag", updatetag);
        return "assignment";
    }


    @PostMapping("/assignment")
    public Assignment createAssignment(@Valid Assignment assignment) {
        return assignmentRepo.save(assignment);
    }

    private AssignmentRepository AssignmentRepo;


    private List<Assignment> fiches;

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/allassignments")
    public String getAllAssingments(Model model) {
        fiches = AssignmentRepo.findAll();
        model.addAttribute("assignments", fiches);

        return "listAllAssignments";
    }
}