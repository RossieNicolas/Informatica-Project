package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Pager;
import com.architec.crediti.models.Tag;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.TagRepo;
import com.architec.crediti.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AssignmentController {

    private Iterable<Assignment> fiches;

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
         assignmentRepo.save(assignment);
         return "redirect:/assignments";
    }
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
    public ModelAndView showPersonsPage(@RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("listAllAssignments");
        fiches = assignmentRepo.findAll();
        int initialPage = 1;
        int pageSize = 20;

        int buttons = (int) assignmentRepo.count() / pageSize;

        if (assignmentRepo.count() % pageSize != 0) {
            buttons++;
        }

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? initialPage : page.get() - 1;

        Page<Assignment> fiches = assignmentRepo.findAll(PageRequest.of(evalPage, pageSize));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        modelAndView.addObject("persons", fiches);
        modelAndView.addObject("assignments", fiches);
        modelAndView.addObject("selectedPageSize", pageSize);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    @RequestMapping(value = "/myassignments", method = RequestMethod.GET)
    public String assignments(Model model) {
        Iterable<Assignment> assignments = assignmentRepo.findAll();

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

    //delete specific assignment
    @GetMapping("/deletemyassignment/{assignmentId}")
    public String deleteMyAssignment(@PathVariable("assignmentId") int assignmentId, Model model) {
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        assignmentRepo.delete(assignment);
        model.addAttribute("assignments", assignmentRepo.findAll());
        return "redirect:/myassignments";
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