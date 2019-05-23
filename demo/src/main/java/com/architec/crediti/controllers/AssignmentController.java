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
import com.architec.crediti.utils.AssignmentMethods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class AssignmentController {

    private static final int PAGE_SIZE = 15;
    private static final int INITAL_PAGE = 0;
    private Log log = LogFactory.getLog(this.getClass());

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
    public String tag(Model model, Principal principal) {
        List<Tag> tags = tagRepo.findAll();

        model.addAttribute("tags", tags);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/assignment";
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
                        assignment.getTitle(), currentUser.getEmail(), "http://vps092.ap.be/allassignments"));
        return "assignments/successfullAssignment";
    }

    // list all assignments
    @GetMapping("/allassignments")
    public ModelAndView getAllAssignments(Model model, @RequestParam("page") Optional<Integer> page, Principal principal) {
        ModelAndView view = new ModelAndView("/assignments/listAllAssignments");
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;

        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        Page<Assignment> fiches = assignmentRepo.findByFullOrderByAssignmentIdDesc(false, PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        view.addObject("assignments", fiches);
        view.addObject("persons", fiches);
        view.addObject("pager", pager);
        view.addObject("tags", tagRepo.findAll());

        return view;
    }

    // list all assignments which are full
    @GetMapping("/allFullAssignments")
    public String getAllFullAssignment(Model model, @RequestParam("page") Optional<Integer> page, Principal principal) {

        List<Assignment> fullas = new ArrayList<>();
        for (Assignment item : assignmentRepo.findAll()) {
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

        Page<Assignment> fiches = assignmentRepo.findByFullOrderByAssignmentIdDesc(false, PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        model.addAttribute("persons", fiches);
        model.addAttribute("pager", pager);

        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listAllFullAssignments";
    }

    // list all unvalidated assignments
    @GetMapping("/unvalidatedassignments")
    public String getUnvalidatedAssingments(Model model, Principal principal) {
        Iterable<Assignment> fiches = assignmentRepo.findAll();
        List<Assignment> unvalidatedFiches = new ArrayList<>();

        for (Assignment item : fiches) {
            if (!item.isValidated()) {
                unvalidatedFiches.add(item);
            }
        }

        model.addAttribute("assignments", unvalidatedFiches);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/listUnvalidatedAssignments";
    }

    // search assignments
    @PostMapping("/allassignments")
    public String getAssignment(@RequestParam("searchbar") String name,
                                Model model, @RequestParam("page") Optional<Integer> page,
                                @RequestParam(required = false, value = "tag") int[] tags) {

        Page<Assignment> fiches = null;
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;

        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        if (tags == null) {
            try {
                Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(name)));
                if (a.getAmountStudents() != a.getMaxStudents() && !a.isArchived()) {
                    model.addAttribute("assignments", a);
                    List<Assignment> list = new ArrayList();
                    list.add(a);
                    fiches = new PageImpl<>(list);
                }

            } catch (Exception e) {

                fiches = AssignmentMethods.removeFullAssignmentsPage(assignmentRepo.findByTitleContainingAndArchived(name, false, PageRequest.of(evalPage, PAGE_SIZE)));
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
            list2 = list2.stream().distinct().collect(Collectors.toList());
            fiches = new PageImpl<>(list2);
            model.addAttribute("assignments", list2);
        }

        Pager pager = null;
        if (fiches != null) {
            pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        }

        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        return "/assignments/listAllAssignments";

    }


    @GetMapping("/myassignments")
    public String assignments(Principal principal, Model model) {
        Student currentStudent = studentRepo.findByUserId(userRepo.findByEmail(principal.getName()));
        Set<Assignment> myAssignments = currentStudent.getAssignments();
        model.addAttribute("assignments", myAssignments);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/myassignments";
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
            log.error(ex.toString());
        }

        boolean roles = false;

        if (as.getAssignerUserId() == user.getUserId() || user.getRole() == Role.COORDINATOR) {
            roles = true;
        }

        model.addAttribute("roles", roles);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "assignments/updateAssignment";
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
        ArrayList<String> listNames = new ArrayList<>();

        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));

        ArchivedAssignment archivedAssignment = new ArchivedAssignment();
        archivedAssignment.fillArchivedAssignment(assignment);

        if (assignment.getTags() != null) {
            for (Tag item : assignment.getTags()) {
                Tag tag = tagRepo.findBytagId(item.getTagId());
                list.add(tag.getTagId());
                listNames.add(tag.getTagName());
            }
            archivedAssignment.setTagIds(list.toString());
            archivedAssignment.setTagName(listNames.toString());
        }

        archiveRepo.save(archivedAssignment);
        for (Student student : assignment.getAssignStudents()) {
            for (Assignment studAssign : student.getAssignments()) {
                if (studAssign.getAssignmentId() == assignment.getAssignmentId()) {
                    Set<Assignment> set = student.getAssignments();
                    for (Iterator<Assignment> iterator = set.iterator(); iterator.hasNext(); ) {
                        Assignment a = iterator.next();
                        if (a.getAssignmentId() == assignment.getAssignmentId()) {
                            iterator.remove();
                            studentRepo.save(student);
                        }
                    }
                }
            }
        }
        assignmentRepo.delete(assignment);

        model.addAttribute("assignments", assignmentRepo.findAll());

        //TODO vervang 'to' door mail van coordinator
        mail.sendSimpleMessage("alina.storme@student.ap.be", "Opdracht gearchiveerd",
                EmailTemplates.archivedAssignment(assignment.getAssigner(),
                        assignment.getTitle(), currentUser.getEmail(), "http://vps092.ap.be/allassignments"));

        return "redirect:/allassignments";
    }

}