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

import com.architec.crediti.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AssignmentController {

    private static final int PAGE_SIZE = 15;
    private static final int INITAL_PAGE = 0;

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
    public String createAssignment( Principal principal, @Valid Assignment assignment,
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
    public ModelAndView getAllAssignments(Model model ,@RequestParam("page") Optional<Integer> page) {
        ModelAndView view = new ModelAndView("listAllAssignments");

        List<Assignment> fullas = new ArrayList<>();
        for (Assignment item: assignmentRepo.findAll()) {
            if (item.getAmountStudents() != item.getMaxStudents() && !item.isArchived()) {

                fullas.add(item);
            }
        }
        model.addAttribute("assignments", fullas);

        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;

        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        Page<Assignment> fiches = assignmentRepo.findAll(PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        view.addObject("assignments", fiches);
        view.addObject("persons", fiches);
        view.addObject("pager", pager);
        view.addObject("tags", tagRepo.findAll());

        return view;
    }

    // list all assignments which are full
    @GetMapping("/allFullAssignments")
    public String getAllFullAssignment(Model model , @RequestParam("page") Optional<Integer> page) {

        List<Assignment> fullas = new ArrayList<>();
        for (Assignment item: assignmentRepo.findAll()) {
            if (item.getAmountStudents() == item.getMaxStudents() && !item.isArchived()) {

                fullas.add(item);
            }
        }
        model.addAttribute("assignments", fullas);

        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;

        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        Page<Assignment> fiches = assignmentRepo.findAll(PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        model.addAttribute("persons", fiches);
        model.addAttribute("pager", pager);

        return "listAllFullAssignments";
    }

    // list all unvalidated assignments
    @GetMapping("/unvalidatedassignments")
    public String getUnvalidatedAssingments(Model model) {
        Iterable<Assignment> fiches = assignmentRepo.findAll();
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
    public String getAssignment(@RequestParam("searchbar") String name,
                         Model model, @RequestParam("page") Optional<Integer> page,
                         @RequestParam(required = false , value = "tag") int[] tags) {

        if(tags == null){
            try {
                Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(name)));
                if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                    model.addAttribute("assignments", a);
                }

            } catch (Exception e) {
                model.addAttribute("assignments", AssignmentMethods.removeFullAssignments(assignmentRepo.findByTitleContainingAndArchived(name, false)));
            }
        }
        else{
            List<Assignment> list = AssignmentMethods.removeFullAssignments(assignmentRepo.findByTitleContainingAndArchived(name , false));
            List<Assignment> list2 = new ArrayList<>();
            for (int item : tags) {
                for (Assignment a : list) {
                    if(a.getTags().contains(tagRepo.findBytagId(item))){
                        list2.add(a);
                    }
                }
            }
            list2 = list2.stream().distinct().collect(Collectors.toList());

            model.addAttribute("assignments" , list2);
        }

        ModelAndView modelAndView = new ModelAndView("listAllAssignments");

        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;

        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        Page<Assignment> fiches = assignmentRepo.findAll(PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        return "listAllAssignments";

    }


    @GetMapping("/myassignments")
    public String assignments(Principal principal, Model model) {
        Student currentStudent = studentRepo.findByUserId(userRepo.findByEmail(principal.getName()));
        Set<Assignment> myAssignments = currentStudent.getAssignments();
        model.addAttribute("assignments", myAssignments);

        return "myassignments";
    }

    // find specific assignment to edit out of all assignments
    @GetMapping("/allassignments/{assignmentId}")
    public String getAssignmentsToUpdate(@PathVariable("assignmentId") int assignmentId, Model model, Principal principal) {
        List<Tag> updatetag = tagRepo.findAll();
        User user = userRepo.findByEmail(principal.getName());
        Student student = studentRepo.findByUserId(user);
        Assignment as = assignmentRepo.findByAssignmentId(assignmentId);
        boolean ingeschreven = false;
        boolean volzet = false;


        for (Assignment item : student.getAssignments()) {
            if (item.getAssignmentId() == assignmentId) {
                ingeschreven = true;
            }
        }
        if (as.getAmountStudents() == as.getMaxStudents()) {
            volzet = true;
        }
        model.addAttribute("volzet", volzet);
        model.addAttribute("ingeschreven", ingeschreven);
        model.addAttribute("updatetag", updatetag);
        try {
            Assignment a = assignmentRepo.findByAssignmentId(assignmentId);
            Set<Tag> tags = a.getTags();
            boolean[] status = new boolean[updatetag.size()];
            String type = a.getType();

            for (int i = 0; i < updatetag.size(); i++) {
                for (Tag item : tags) {
                    if (updatetag.get(i).getTagId() == item.getTagId()) {
                        status[i] = true;
                    }
                }
            }

            model.addAttribute("type", type);
            model.addAttribute("status", status);
            if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                model.addAttribute("assignments", a);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        boolean roles = false;

        if (as.getAssignerUserId() == user.getUserId() || user.getRole() == Role.COORDINATOR) {
            roles = true;
        }

        model.addAttribute("roles", roles);
        return "updateAssignment";
    }

    // update specific assignment
    @PostMapping(value = "/allassignments/{assignmentId}")
    public String updateAssignment(@PathVariable("assignmentId") int assignmentId,
                                   @Valid Assignment assignment, @RequestParam(required = false, value = "tag") int[] tags) {
        Set<Tag> set = new HashSet<>();
        Assignment a = assignmentRepo.findByAssignmentId(assignmentId);

        if (tags != null) {
            for (int item : tags) {
                Tag tag = tagRepo.findBytagId(item);
                set.add(tag);
            }
        }

        assignment.setTags(set);
        assignment.setAssignerUserId(userRepo.findByUserId(a.getAssignerUserId()));
        assignment.setAssignmentId(assignmentId);
        assignmentRepo.save(assignment);
        return "redirect:/allassignments";
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
        ArrayList<Integer> list = new ArrayList<>();

        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));

        ArchivedAssignment archivedAssignment = new ArchivedAssignment();
        archivedAssignment.fillArchivedAssignment(assignment);

        if (assignment.getTags() != null) {
            for (Tag item : assignment.getTags()) {
                Tag tag = tagRepo.findBytagId(item.getTagId());
                list.add(tag.getTagId());
            }
            archivedAssignment.setTagIds(list.toString());
        }

        archiveRepo.save(archivedAssignment);
        assignmentRepo.delete(assignment);

        model.addAttribute("assignments", assignmentRepo.findAll());

        //TODO vervang 'to' door mail van coordinator
        mail.sendSimpleMessage("alina.storme@student.ap.be", "Opdracht gearchiveerd",
                EmailTemplates.archivedAssignment(assignment.getAssigner(),
                        assignment.getTitle(), currentUser.getEmail(), "http://vps092.ap.be/allassignments",
                        "class group"));

        return "redirect:/allassignments";
    }

}