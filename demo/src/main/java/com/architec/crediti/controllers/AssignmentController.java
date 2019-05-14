package com.architec.crediti.controllers;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.models.*;
import com.architec.crediti.repositories.*;

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
    final private int initialPage = 0;
    final private int pageSize = 15;

    @Autowired
    TagRepo tagRepo;

    @Autowired
    AssignmentRepository assignmentRepo;

    @Autowired
    UserRepository userRepo;
    @Autowired
    ArchiveRepository archiveRepo;

    @Autowired
    EmailServiceImpl mail;

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

        mail.sendSimpleMessage("alina.storme@student.ap.be", "Nieuwe opdracht gecreëerd",
        EmailTemplates.createdAssignment(assignment.getAssigner(),
                assignment.getTitle(), currentUser.getEmail(), "http://vps092.ap.be/allassignments", "class group"));
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
        modelAndView.addObject("assignments", fiches);
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
    String getAssignment(@RequestParam("searchbar") String name, Model model , @RequestParam("page") Optional<Integer> page) {

        try {
            Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(name)));
            if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                model.addAttribute("assignments", a);
            }

        } catch (Exception e) {
            model.addAttribute("assignments", Methods.removeFullAssignments(assignmentRepo.findByTitleContainingAndArchived(name, false)));
        }

        ModelAndView modelAndView = new ModelAndView("listAllAssignments");
        fiches = assignmentRepo.findAll();


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

        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", pageSize);
        model.addAttribute("pager", pager);

        return "listAllAssignments";
    }

    @GetMapping(value = "/myassignments")
    public String assignments(Principal principal, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());
        Iterable<Assignment> assignments = assignmentRepo.findAll();
        ArrayList<Assignment> myAssignments = new ArrayList<>();
        for (Assignment a : assignments){
            if (currentUser.getUserId() == a.getAssignerUserId()) {
                myAssignments.add(a);
            }
        }
        model.addAttribute("assignments", myAssignments);

        return "myassignments";
    }

    // find specific assignment to edit out of all assignments
    @GetMapping(value = "/allassignments/{assignmentId}")
    public String getAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model) {
        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
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
        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
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
        return "updateMyAssignment";
    }

    // update specific assignment
    @PostMapping(value = "/myassignments/{assignmentId}")
    public String updateMyAssignment(Principal principal, @PathVariable("assignmentId") int assignmentId,
            @Valid Assignment assignment) {
        User currentUser = userRepo.findByEmail(principal.getName());
        assignment.setAssignerUserId(currentUser);
        assignment.setAssignmentId(assignmentId);
        assignmentRepo.save(assignment);
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
    public String validateAssignment(@PathVariable("assignmentId") int assignmentId, Model model, Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());

        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        assignment.setValidated(true);
        assignmentRepo.save(assignment);
        model.addAttribute("assignments", assignmentRepo.findAll());

        //mail to coordinator
        mail.sendSimpleMessage("alina.storme@student.ap.be", "Opdracht gevalideerd",
                EmailTemplates.validatedAssignment(assignment.getTitle()));

        //mail to student
        mail.sendSimpleMessage("alina.storme@student.ap.be", "Opdracht gevalideerd",
                EmailTemplates.validatedAssignmentStudent(assignment.getTitle()));

        return "redirect:/allassignments";
    }

    //archive assignment
    @GetMapping("/archiveassignment/{assignmentId}")
    public String archiveAssignment(Principal principal, @PathVariable("assignmentId") int assignmentId, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        assignment.setArchived(true);
        assignmentRepo.save(assignment);
        ArchivedAssignment archivedAssignment = new ArchivedAssignment();
        archivedAssignment.fillArchivedAssignment(assignment);
        archiveRepo.save(archivedAssignment);
        assignmentRepo.delete(assignment);
        model.addAttribute("assignments", assignmentRepo.findAll());

        //mail to coordinator
        mail.sendSimpleMessage("alina.storme@student.ap.be", "Opdracht gearchiveerd",
                EmailTemplates.archivedAssignment(assignment.getAssigner(),
                        assignment.getTitle(), currentUser.getEmail(), "http://vps092.ap.be/allassignments", "class group"));

        return "redirect:/allassignments";
    }

}