package com.architec.crediti.controllers;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.Enrolled;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Controller
public class EnrollController {

    private final
    AssignmentRepository assignmentRepo;

    private final
    StudentRepository studentRepo;

    private final
    UserRepository userRepo;

    private final
    EnrolledRepository enrolledRepo;


    private final
    EmailServiceImpl mail;

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    public EnrollController(AssignmentRepository assignmentRepo, StudentRepository studentRepo,
                            UserRepository userRepo, EnrolledRepository enrolledRepo, EmailServiceImpl mail) {
        this.assignmentRepo = assignmentRepo;
        this.studentRepo = studentRepo;
        this.userRepo = userRepo;
        this.enrolledRepo = enrolledRepo;
        this.mail = mail;
    }


    // assign student to specific assignment
    @GetMapping("/studentenroll/{assignmentId}")
    public String enrollAssignment(Model model, @PathVariable("assignmentId") int assignmentId,
                                   Principal principal) {
        Assignment assignment = assignmentRepo.findById((long) assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid assignment Id:" + assignmentId));
        User user = userRepo.findByEmail(principal.getName());
        Student student = studentRepo.findByUserId(user);
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

            if (!zelfde) {
                if (assignment.getAmountStudents() < assignment.getMaxStudents()) {
                    set.add(assignment);
                    assignment.setAmountStudents(counter + 1);
                    assignmentRepo.save(assignment);
                    if (assignment.getAmountStudents() == assignment.getMaxStudents()) {
                        assignment.setFull(true);
                        assignmentRepo.save(assignment);
                    }
                }
            } else return "assignments/alreadyAssigned";

            Enrolled enrolled = new Enrolled(user.getFirstname() + " " + user.getLastname(), user.getEmail(), assignment.getAssignmentId(), assignment.getTitle(), user.getUserId());
            enrolledRepo.save(enrolled);

            student.setAssignments(set);
            studentRepo.save(student);

            mail.sendSimpleMessage(student.getEmail(), "Inschrijving opdracht",
                    EmailTemplates.waitValidationEnrolledAssignmentStudent(assignment.getTitle()));
            mail.sendSimpleMessage(assignerEmail, "Inschrijving opdracht",
                    EmailTemplates.enrolledAssignment(assignment.getTitle(), student.getUserId().toString(), student.getEmail()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "enroll/studentenroll";
    }

    // show list of unapproved assignments and make possible to approve
    @GetMapping("/unapprovedEnrollments")
    public String unapprovedEnroll(Model model, Principal principal) {
        List<Enrolled> unapproved = enrolledRepo.findAll();
        model.addAttribute("unapproved", unapproved);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "enroll/listUnapprovedEnrollment";
    }


    @GetMapping("/approveEnroll/{id}")
    public String validateEnroll(@PathVariable("id") int enrolledid) {
        Enrolled enrolled = enrolledRepo.findByEnrolledId(enrolledid);
        Student student = studentRepo.findByUserId(userRepo.findByUserId(enrolled.getUserId()));
        enrolledRepo.delete(enrolled);

        mail.sendSimpleMessage(student.getEmail(), "Inschrijving gevalideerd",
                EmailTemplates.approvedEnrolledAssignmentStudent(enrolled.getTitle()));

        return "redirect:/unapprovedEnrollments";
    }

    @GetMapping("/deleteEnroll/{id}")
    public String deleteEnroll(@PathVariable("id") int enrolledid) {
        Enrolled enrolled = enrolledRepo.findByEnrolledId(enrolledid);
        enrolledRepo.delete(enrolled);

        Student student = studentRepo.findByUserId(userRepo.findByUserId(enrolled.getUserId()));
        for (Assignment item : student.getAssignments()) {
            if (item.getAssignmentId() == enrolled.getAssignment()) {
                Set<Assignment> set = student.getAssignments();
                for (Iterator<Assignment> iterator = set.iterator(); iterator.hasNext(); ) {
                    Assignment a = iterator.next();
                    if (a.getAssignmentId() == enrolled.getAssignment()) {
                        iterator.remove();
                        studentRepo.save(student);

                        mail.sendSimpleMessage(student.getEmail(), "Inschrijving geweigerd",
                                EmailTemplates.declinedEnrolledAssignmentStudent(enrolled.getTitle()));
                        return "redirect:/unapprovedEnrollments";
                    }
                }
            }
        }
        return "redirect:/unapprovedEnrollments";
    }
}