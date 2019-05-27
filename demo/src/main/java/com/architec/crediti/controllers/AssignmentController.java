package com.architec.crediti.controllers;

import ch.qos.logback.core.rolling.helper.TimeBasedArchiveRemover;
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
import java.util.stream.Collectors;

@Controller
public class AssignmentController {

    private static final int PAGE_SIZE = 15;
    private static final int INITAL_PAGE = 0;
    private Log log = LogFactory.getLog(this.getClass());
    private String name = "";
    private int[] tags;
    List<Long> list =null;
    private Page<Assignment> fiches = null;


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
        ModelAndView view = new ModelAndView("assignments/listAllAssignments");
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;
        Page<Assignment> fiches = null;
        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        if(this.list != null){
            fiches = assignmentRepo.findByTagsOrderByAssignmentIdDesc(this.list, PageRequest.of(evalPage, PAGE_SIZE));
        }
        else{
            fiches =assignmentRepo.findByTitleContainingAndFullOrderByAssignmentIdDesc(name, false,PageRequest.of(evalPage, PAGE_SIZE));
        }
        model.addAttribute("assignments",fiches);
        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);

        model.addAttribute("selectedPageSize", PAGE_SIZE);
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

        this.tags = tags;
        this.name = name;
        int buttons = (int) assignmentRepo.count() / PAGE_SIZE;

        if (assignmentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }

        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;
        if (tags == null) {
            this.list = null;
            try {
                Assignment a = assignmentRepo.findByAssignmentId((Integer.parseInt(name)));
                if (a.getAmountStudents() != a.getMaxStudents()) {
                    model.addAttribute("assignments", a);
                    List<Assignment> listsd = new ArrayList();
                    listsd.add(a);
                    fiches = new PageImpl<>(listsd);
                }

            } catch (Exception e) {

                fiches = assignmentRepo.findByTitleContainingAndFullOrderByAssignmentIdDesc(name, false, PageRequest.of(evalPage, PAGE_SIZE));
                model.addAttribute("assignments", fiches);
            }
        } else {
            List<Assignment> list3 = (List<Assignment>) assignmentRepo.findByTitleContainingAndFullOrderByAssignmentIdDesc(name, false);
            list = new ArrayList<>();
            for (int item : tags) {
                for (Assignment a : list3) {
                    if (a.getTags().contains(tagRepo.findBytagId(item))) {
                        list.add(a.getAssignmentId());
                    }
                }
            }
            list = list.stream().distinct().collect(Collectors.toList());
            fiches = assignmentRepo.findByTagsOrderByAssignmentIdDesc(list, PageRequest.of(evalPage, PAGE_SIZE));
            model.addAttribute("assignments", fiches);
        }

        Pager pager = new Pager(fiches.getTotalPages(), fiches.getNumber(), buttons);
        model.addAttribute("persons", fiches);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        model.addAttribute("tags", tagRepo.findAll());
        return "assignments/listAllAssignments";

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
        boolean roles2 = false;

        if (as.getAssignerUserId() == user.getUserId() || user.getRole() == Role.COORDINATOR) {
            roles = true;
        }

        if (as.getAssignerUserId() == user.getUserId() || user.getRole() == Role.COORDINATOR || user.getRole() == Role.DOCENT) {
            roles2 = true;
        }

        model.addAttribute("roles", roles);
        model.addAttribute("roles2", roles2);
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
    public String archiveAssignment(Principal principal, @PathVariable("assignmentId") long assignmentId, Model model, @RequestParam(required = false, value = "tag") int[] tags) {
        User currentUser = userRepo.findByEmail(principal.getName());
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<String> listNames = new ArrayList<>();

        Assignment assignment = assignmentRepo.findById(assignmentId)
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

    //duplicate assignment
    @GetMapping("/duplicateassignment/{assignmentId}")
    public String duplicateAssignment(Principal principal, @PathVariable("assignmentId") long assignmentId, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());

        Assignment assignment = assignmentRepo.findByAssignmentId(assignmentId);
        if (assignment == null) {
            ArchivedAssignment archivedAss = archiveRepo.findByAssignmentId(assignmentId);
            String title = archivedAss.getTitle();
            String type = archivedAss.getType();
            String omschrijving = archivedAss.getTask();
            String beginDat = archivedAss.getStartDate();
            String eindDat = archivedAss.getEndDate();
            int totaalUur = archivedAss.getAmountHours();
            int maxStud = archivedAss.getMaxStudents();

            ArchivedAssignment duplicateAssignment = new ArchivedAssignment(title, type, omschrijving, totaalUur, maxStud, beginDat,
                    eindDat, currentUser.toString());

            List<Tag> tags = tagRepo.findAll();

            model.addAttribute("tags", tags);
            model.addAttribute("type", type);
            model.addAttribute("duplicate", duplicateAssignment);

            boolean[] status = new boolean[tags.size()];
            String selectedTags = archivedAss.getTagIds();
            List<String> items = Arrays.asList(selectedTags.replace("[", "").replace("]", "").split("\\s*,\\s*"));

            if (!items.get(0).equals("")) {
                for (int i = 0; i < tags.size(); i++) {
                    for (String item : items) {
                        if (tags.get(i).getTagId() == Integer.parseInt(item)) {
                            status[i] = true;
                        }
                    }
                }
            }

            model.addAttribute("status", status);

            return "assignments/duplicateAssignment";

        } else {
            String title = assignment.getTitle();
            String type = assignment.getType();
            String omschrijving = assignment.getTask();
            String beginDat = assignment.getStartDate();
            String eindDat = assignment.getEndDate();
            int totaalUur = assignment.getAmountHours();
            int maxStud = assignment.getMaxStudents();

            Assignment duplicateAssignment = new Assignment(title, type, omschrijving, totaalUur, maxStud, beginDat,
                    eindDat, false, false, currentUser);

            List<Tag> tags = tagRepo.findAll();

            model.addAttribute("tags", tags);
            model.addAttribute("type", type);
            model.addAttribute("duplicate", duplicateAssignment);

            boolean[] status = new boolean[tags.size()];
            Set<Tag> selectedTags = assignment.getTags();

            for (int i = 0; i < tags.size(); i++) {
                for (Tag item : selectedTags) {
                    if (tags.get(i).getTagId() == item.getTagId()) {
                        status[i] = true;
                    }
                }
            }
            model.addAttribute("status", status);

            return "assignments/duplicateAssignment";
        }
    }

    // add duplicate assignment
    @PostMapping("/duplicateassignment/{assignmentId}")
    public String createDuplicate(Principal principal, @Valid Assignment assignment,
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
}