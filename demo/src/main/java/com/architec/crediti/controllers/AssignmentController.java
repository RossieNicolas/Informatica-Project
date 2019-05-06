package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Tag;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.awt.ModalExclude;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    TagRepo tagRepo;

    @Autowired
    AssignmentRepository assignmentRepo;

    //get assignment form (if no tags)
    @RequestMapping("/assignment")
    public String assignment() {
        return "assignment";
    }

    //get assignment form
    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public String tag(Model model) /* throws SQLException */ {

        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
        return "assignment";
    }

    //add assignment
    @PostMapping("/assignment")
    public String createAssignment(@Valid Assignment assignment) {
         assignmentRepo.save(assignment);
         return "redirect:/assignments";
    }

    //error page
    @GetMapping("/error")
    public String error() {
        return "error";
    }

    //list all assignments
    @GetMapping("/allassignments")
    public String getAllAssingments(Model model) {
        List<Assignment> fiches = assignmentRepo.findAll();
        model.addAttribute("assignments", fiches);

        return "listAllAssignments";
    }

    //all my assignments
    @RequestMapping(value = "/myassignments", method = RequestMethod.GET)
    public String assignments(Model model) {
        List<Assignment> assignments = assignmentRepo.findAll();

        model.addAttribute("assignments", assignments);

        return "myassignments";
    }


    //find specific assignment to edit out of all assignments
    @RequestMapping(value = "/allassignments/{assignmentId}", method = RequestMethod.GET)
    public String getAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model) {

        List<Assignment> fiches = assignmentRepo.findByAssignmentId(assignmentId);

        model.addAttribute("assignments", fiches);
        return "updateAssignment";
    }

    //update specific assignment
    @PostMapping(value = "/allassignments/{assignmentId}")
    public String updateAssignment(@PathVariable("assignmentId") int assignmentId, @Valid Assignment assignment) {

        assignment.setAssignmentId(assignmentId);
        if (!(assignment.getTitle().equalsIgnoreCase("") || assignment.getType().equalsIgnoreCase("") || assignment.getTask().equalsIgnoreCase(""))){
            assignmentRepo.save(assignment);
        }

        return "redirect:/allassignments";
    }

    //find specific assignment to edit out of all assignments
    @RequestMapping(value = "/myassignments/{assignmentId}", method = RequestMethod.GET)
    public String getMyAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model) {

        List<Assignment> fiches = assignmentRepo.findByAssignmentId(assignmentId);

        model.addAttribute("assignments", fiches);
        return "updateMyAssignment";
    }

    //update specific assignment
    @PostMapping(value = "/myassignments/{assignmentId}")
    public String updateMyAssignment(@PathVariable("assignmentId") int assignmentId, @Valid Assignment assignment) {

        assignment.setAssignmentId(assignmentId);
        if (!(assignment.getTitle().equalsIgnoreCase("") || assignment.getType().equalsIgnoreCase("") || assignment.getTask().equalsIgnoreCase(""))){
            assignmentRepo.save(assignment);
        }
        return "redirect:/myassignments";
    }

    //delete specific assignment
    @GetMapping("/deleteassignment/{assignmentId}")
    public String deleteAssignment(@PathVariable("assignmentId") int assignmentId, Model model) {
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        assignmentRepo.delete(assignment);
        model.addAttribute("assignments", assignmentRepo.findAll());
        return "redirect:/allassignments";
    }

    //validate specific assignment
    @GetMapping("/validateassignment/{assignmentId}")
    public  String validateAssignment(@PathVariable("assignmentId") int assignmentId, Model model){
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        assignment.setValidated(true);
        assignmentRepo.save(assignment);
        model.addAttribute("assignments", assignmentRepo.findAll());
        return "redirect:/allassignments";
    }

}