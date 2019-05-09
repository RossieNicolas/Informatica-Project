package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Pager;
import com.architec.crediti.models.Tag;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.Methods;
import com.architec.crediti.repositories.TagRepo;
import com.architec.crediti.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
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

    // get assignment form
    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public String tag(Model model) /* throws SQLException */ {

        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
        return "assignment";
    }

    // add assignment
    @PostMapping("/assignment")
    public String createAssignment(Principal principal, @Valid Assignment assignment,
            @RequestParam(required = false, value = "tag") int[] tags) {
        ArrayList<Tag> list = new ArrayList<>();
        ArrayList<String> list2 = new ArrayList<>();
        User currentUser = userRepo.findByEmail(principal.getName());

        if (tags != null) {
            for (int item : tags) {
                Tag tag = tagRepo.findBytagId(item);
                list.add(tag);
            }

            for (Tag item : list) {
                list2.add(item.getTagName());
            }
            assignment.setTagAssign(list2.toString());
        }
        assignment.setAssignerUserId(currentUser);
        assignmentRepo.save(assignment);
        return "successfullAssignment";
    }

    // error page
    @GetMapping("/error")
    public String error() {
        return "error";
    }

    // list all assignments
    @GetMapping("/allassignments")
    public ModelAndView showPersonsPage(@RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("listAllAssignments");
        fiches = assignmentRepo.findAll();
        int initialPage = 0;
        int pageSize = 15;

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
        modelAndView.addObject("assignments",
                Methods.removeFullAssignments(assignmentRepo.findByTitleContainingAndArchived("", false)));
        modelAndView.addObject("selectedPageSize", pageSize);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    // list all unvalidated assignments
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

    // search assignments
    @PostMapping("/allassignments")
    String getAssignment(@RequestParam("searchbar") String name, Model model) {

        try {
            Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(name)));
            if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                model.addAttribute("assignments", a);
            }

        } catch (Exception e) {
            model.addAttribute("assignments",
                    Methods.removeFullAssignments(assignmentRepo.findByTitleContainingAndArchived(name, false)));
        }

        return "listAllAssignments";
    }

    @GetMapping(value = "/myassignments")
    public String assignments(Model model) {
        Iterable<Assignment> assignments = assignmentRepo.findAll();

        model.addAttribute("assignments", assignments);

        return "myassignments";
    }

    // find specific assignment to edit out of all assignments
    @GetMapping(value = "/allassignments/{assignmentId}")
    public String getAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model) {

        try {
            Assignment a = assignmentRepo.findByAssignmentId(assignmentId);
            if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                model.addAttribute("assignments", a);
            }
        } catch (Exception ex) {
            // als er geen assignment is met ingegeven id dan wordt er een lege pagina
            // weergegeven,
            // zonder catch krijgt gebruiker een error wat niet de bedoeling is
        }
        return "updateAssignment";
    }

    // update specific assignment
    @PostMapping(value = "/allassignments/{assignmentId}")
    public String updateAssignment(Principal principal, @PathVariable("assignmentId") int assignmentId,
            @Valid Assignment assignment) {
        User currentUser = userRepo.findByEmail(principal.getName());
        assignment.setAssignerUserId(currentUser);
        assignment.setAssignmentId(assignmentId);

        assignmentRepo.save(assignment);

        return "redirect:/allassignments";
    }

    // find specific assignment to edit out of all assignments
    @RequestMapping(value = "/myassignments/{assignmentId}", method = RequestMethod.GET)
    public String getMyAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model) {

        Assignment fiches = assignmentRepo.findByAssignmentId(assignmentId);

        model.addAttribute("assignments", fiches);
        return "updateMyAssignment";
    }

    // update specific assignment
    @PostMapping(value = "/myassignments/{assignmentId}")
    public String updateMyAssignment(Principal principal, @PathVariable("assignmentId") int assignmentId,
            @Valid Assignment assignment) {
        User currentUser = userRepo.findByEmail(principal.getName());
        assignment.setAssignerUserId(currentUser);
        assignment.setAssignmentId(assignmentId);
        if (!(assignment.getTitle().equalsIgnoreCase("") || assignment.getType().equalsIgnoreCase("")
                || assignment.getTask().equalsIgnoreCase(""))) {
            assignmentRepo.save(assignment);
        }
        return "redirect:/myassignments";
    }

    // delete specific assignment
    @GetMapping("/deleteassignment/{assignmentId}")
    public String deleteAssignment(@PathVariable("assignmentId") int assignmentId, Model model) {
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        assignmentRepo.delete(assignment);
        model.addAttribute("assignments", assignmentRepo.findAll());
        return "redirect:/allassignments";
    }

    // validate specific assignment
    @GetMapping("/validateassignment/{assignmentId}")
    public String validateAssignment(@PathVariable("assignmentId") int assignmentId, Model model) {
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        assignment.setValidated(true);
        assignmentRepo.save(assignment);
        model.addAttribute("assignments", assignmentRepo.findAll());
        return "redirect:/allassignments";
    }

}