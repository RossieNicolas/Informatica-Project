package com.architec.crediti.controllers;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.models.*;
import com.architec.crediti.repositories.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;


import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
public class AssignmentController {

    private Iterable<Assignment> fiches;

    private final
    TagRepo tagRepo;

    private final
    AssignmentRepository assignmentRepo;

    private final
    StudentRepository studentRepo;

    private final
    UserRepository userRepo;

    private final
    ArchiveRepository archiveRepo;

    private final
    EmailServiceImpl mail;

    @Autowired
    public AssignmentController(TagRepo tagRepo, AssignmentRepository assignmentRepo, StudentRepository studentRepo,
                                UserRepository userRepo, ArchiveRepository archiveRepo, EmailServiceImpl mail) {
        this.tagRepo = tagRepo;
        this.assignmentRepo = assignmentRepo;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.archiveRepo = archiveRepo;
        this.mail = mail;
    }

    // get assignment form
    @GetMapping("/assignment")
    public String tag(Model model) {
        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
        return "assignment";
    }

    // add assignment
    @PostMapping("/assignment")
    public String createAssignment(Principal principal, @Valid Assignment assignment,
                                   @RequestParam(required = false, value = "tag") int[] tags) {
        Set<Tag> set = new HashSet<>();
        User currentUser = userRepo.findByEmail(principal.getName());

        assignment.setTags(set);
        if (tags != null) {
            for (int item : tags) {
                Tag tag = tagRepo.findBytagId(item);
                set.add(tag);
            }
        }
        assignment.setTags(set);
        assignment.setAssignerUserId(currentUser);
        assignmentRepo.save(assignment);

        mail.sendSimpleMessage("alina.storme@student.ap.be", "Nieuwe opdracht gecreëerd",
                EmailTemplates.createdAssignment(assignment.getAssigner(),
                        assignment.getTitle(), currentUser.getEmail(), "http://vps092.ap.be/allassignments",
                        "class group"));
        return "successfullAssignment";
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
        int evalPage = (page.orElse(0) < 1) ? initialPage : page.get() - 1;

        Page<Assignment> fiches = assignmentRepo.findAll(PageRequest.of(evalPage, pageSize));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        modelAndView.addObject("persons", fiches);
        modelAndView.addObject("assignments", fiches);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("tags", tagRepo.findAll());
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
    String getAssignment(@RequestParam("searchbar") String name,
                         Model model, @RequestParam("page") Optional<Integer> page,
                         @RequestParam(required = false, value = "tag") int[] tags) {

        //if no tags checked search on all assignments
        if (tags == null) {
            try {
                Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(name)));
                if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                    model.addAttribute("assignments", a);
                }


            } catch (Exception e) {
                model.addAttribute("assignments", AssignmentMethods.removeFullAssignments(assignmentRepo.findByTitleContainingAndArchived(name, false)));
            }
        } else {
            List<Assignment> list = AssignmentMethods.removeFullAssignments(assignmentRepo.findByTitleContainingAndArchived(name, false));
            List<Assignment> list2 = new ArrayList<>();
            for (int item : tags) {
                for (Assignment a : list) {
                    if (a.getTags().contains(tagRepo.findBytagId(item))) {
                        list2.add(a);
                    }
                }
            }

            //delete double assignments in search
            list2 = list2.stream().distinct().collect(Collectors.toList());

            model.addAttribute("assignments", list2);
        }

        ModelAndView modelAndView = new ModelAndView("listAllAssignments");
        int pageSize = 15;
        int buttons = (int) assignmentRepo.count() / pageSize;

        if (assignmentRepo.count() % pageSize != 0) {
            buttons++;
        }
        int initialPage = 0;
        int evalPage = (page.orElse(0) < 1) ? initialPage : page.get() - 1;

        Page<Assignment> fiches = assignmentRepo.findAll(PageRequest.of(evalPage, pageSize));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", pageSize);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());

        return "listAllAssignments";
    }

    @GetMapping("/myassignments")
    public String assignments(Principal principal, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());
        Iterable<Assignment> assignments = assignmentRepo.findAll();
        ArrayList<Assignment> myAssignments = new ArrayList<>();
        for (Assignment a : assignments) {
            if (currentUser.getUserId() == a.getAssignerUserId()) {
                myAssignments.add(a);
            }
        }
        model.addAttribute("assignments", myAssignments);

        return "myassignments";
    }

    // find specific assignment to edit out of all assignments
    @GetMapping("/allassignments/{assignmentId}")
    public String getAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model, @Valid Student student, Principal principal) {
        List<Tag> updatetag = tagRepo.findAll();
        User user = userRepo.findByEmail(principal.getName());
        student = studentRepo.findByUserId(user);
        Assignment as = assignmentRepo.findByAssignmentId(assignmentId);
        boolean ingeschreven = false;
        boolean volzet = false;


        for (Assignment item : student.getAssignments()){
            if(item.getAssignmentId() == assignmentId){
                ingeschreven = true;
            }
        }
        if(as.getAmountStudents() == as.getMaxStudents()){
            volzet = true;
        }
        model.addAttribute("volzet", volzet);
        model.addAttribute("ingeschreven", ingeschreven);
        model.addAttribute("updatetag", updatetag);
        try {
            Assignment a = assignmentRepo.findByAssignmentId(assignmentId);
            Set<Tag> tags = a.getTags();
            boolean[] status = new boolean[updatetag.size()];

            for (int i = 0; i < updatetag.size(); i++) {
                for (Tag item : tags) {
                    if (updatetag.get(i).getTagId() == item.getTagId()) {
                        status[i] = true;
                    }
                }
            }

            model.addAttribute("status", status);
            if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                model.addAttribute("assignments", a);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "updateAssignment";
    }

    // update specific assignment
    @PostMapping(value = "/allassignments/{assignmentId}")
    public String updateAssignment(Principal principal, @PathVariable("assignmentId") int assignmentId,
                                   @Valid Assignment assignment, @RequestParam(required = false, value = "tag") int[] tags) {
        Set<Tag> set = new HashSet<>();
        User currentUser = userRepo.findByEmail(principal.getName());

        if (tags != null) {
            for (int item : tags) {
                Tag tag = tagRepo.findBytagId(item);
                set.add(tag);
            }
        }

        assignment.setTags(set);
        assignment.setAssignerUserId(currentUser);
        assignment.setAssignmentId(assignmentId);
        assignmentRepo.save(assignment);
        return "redirect:/allassignments";
    }

    // find specific assignment to edit out of all assignments
    @GetMapping("/myassignments/{assignmentId}")
    public String getMyAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model) {
        List<Tag> updatetag = tagRepo.findAll();

        model.addAttribute("updatetag", updatetag);
        try {
            Assignment a = assignmentRepo.findByAssignmentId(assignmentId);
            Set<Tag> tags = a.getTags();
            boolean[] status = new boolean[updatetag.size()];

            for (int i = 0; i < updatetag.size(); i++) {
                for (Tag item : tags) {
                    if (updatetag.get(i).getTagId() == item.getTagId()) {
                        status[i] = true;
                    }
                }
            }

            model.addAttribute("status", status);
            if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                model.addAttribute("assignments", a);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "updateMyAssignment";
    }

    // update specific assignment
    @PostMapping(value = "/myassignments/{assignmentId}")
    public String updateMyAssignment(Principal principal, @PathVariable("assignmentId") int assignmentId,
                                     @Valid Assignment assignment, @RequestParam(required = false, value = "tag") int[] tags) {
        Set<Tag> set = new HashSet<>();
        User currentUser = userRepo.findByEmail(principal.getName());

        if (tags != null) {
            for (int item : tags) {
                Tag tag = tagRepo.findBytagId(item);
                set.add(tag);
            }
        }

        assignment.setTags(set);
        assignment.setAssignerUserId(currentUser);
        assignment.setAssignmentId(assignmentId);
        assignmentRepo.save(assignment);
        return "redirect:/myassignments";
    }

    // assign student to specific assignment
    @GetMapping("/studentenroll/{assignmentId}")
    public String enrollAssignment(@PathVariable("assignmentId") int assignmentId, @Valid Student student,
                                   Principal principal, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        User user = userRepo.findByEmail(principal.getName());
        student = studentRepo.findByUserId(user);
        long assignerId = assignmentRepo.findByAssignmentId(assignmentId).getAssignerUserId();
        String assignerEmail = userRepo.findByUserId(assignerId).getEmail();

        try {
            Set<Assignment> set = new HashSet<>();
            set.addAll(student.getAssignments());
            int counter = assignment.getAmountStudents();
            boolean zelfde = false;

            for (Assignment item : student.getAssignments()) {
                if (item.getAssignmentId() == assignmentId) {
                    zelfde = true;
                }
            }

            if(!zelfde) {
                if (assignment.getAmountStudents() < assignment.getMaxStudents()) {
                    set.add(assignment);
                    assignment.setAmountStudents(counter + 1);
                }
            }else return "alreadyAssigned";

            student.setAssignments(set);
            studentRepo.save(student);

            mail.sendSimpleMessage(student.getEmail(), "Inschrijving opdracht",
                    EmailTemplates.enrolledAssignmentStudent(assignment.getTitle()));
            mail.sendSimpleMessage(assignerEmail, "Inschrijving opdracht",
                    EmailTemplates.enrolledAssignment(assignment.getTitle(), student.getUserId().toString(), student.getEmail(), "class group"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "studentenroll";
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

        long assignerId = assignmentRepo.findByAssignmentId(assignmentId).getAssignerUserId();
        String assignerEmail = userRepo.findByUserId(assignerId).getEmail();

        mail.sendSimpleMessage(assignerEmail, "Opdracht gevalideerd",
                EmailTemplates.validatedAssignment(assignment.getTitle()));

        return "redirect:/allassignments";
    }

    //archive assignment
    @GetMapping("/archiveassignment/{assignmentId}")
    public String archiveAssignment(Principal principal, @PathVariable("assignmentId") int assignmentId, Model model, @RequestParam(required = false, value = "tag") int[] tags) {
        User currentUser = userRepo.findByEmail(principal.getName());
        Set<Tag> set = new HashSet<>();
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));

        assignmentRepo.save(assignment);
        ArchivedAssignment archivedAssignment = new ArchivedAssignment();
        archivedAssignment.fillArchivedAssignment(assignment);

        /*if (assignment.getTags() != null) {
            for (Tag item : assignment.getTags()) {
                set.add(item);
            }
        }

        archivedAssignment.setTags(set);*/
        assignmentRepo.delete(assignment);
        archiveRepo.save(archivedAssignment);
        //assignment.setArchived(true);

        model.addAttribute("assignments", assignmentRepo.findAll());

        //TODO vervang 'to' door mail van coordinator
        mail.sendSimpleMessage("alina.storme@student.ap.be", "Opdracht gearchiveerd",
                EmailTemplates.archivedAssignment(assignment.getAssigner(),
                        assignment.getTitle(), currentUser.getEmail(), "http://vps092.ap.be/allassignments",
                        "class group"));

        return "redirect:/allassignments";
    }

}