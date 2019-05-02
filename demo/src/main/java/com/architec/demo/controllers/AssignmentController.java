package com.architec.demo.controllers;

import com.architec.demo.models.Assignment;
import com.architec.demo.models.Tag;
import com.architec.demo.repositories.AssignmentRepository;
import com.architec.demo.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<Assignment> fiches = assignmentRepo.findAll();
        model.addAttribute("assignments", fiches);

        return "listAllAssignments";
    }
    @RequestMapping(value = "/updateassignment", method = RequestMethod.GET)
    public String updateAssignment(Model model) {

        List<Assignment> assignments = assignmentRepo.findByAssignmentId(1);
        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);

        model.addAttribute("assignments", assignments);
        return "updateAssignment";
    }
    @RequestMapping(value = "/updatemyassignment", method = RequestMethod.GET)
    public String updateMyAssignment(Model model) {

        List<Assignment> assignments = assignmentRepo.findByAssignmentId(1);
        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);

        model.addAttribute("assignments", assignments);
        return "updateMyAssignment";
    }
    @PostMapping("/updateassignment")
    public String updateAssignment(@Valid Assignment assignment) {


        assignmentRepo.save(assignment);
        return "listAllAssignments";
    }
    @PostMapping("/updatemyassignment")
    public String updateMyAssignment(@Valid Assignment assignment) {

        assignmentRepo.save(assignment);
        return "listAllAssignments";
    }


}