package com.architec.crediti.controllers;

import com.architec.crediti.models.*;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.repositories.StudentRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {

    private final
    UserRepository userRepo;

    private final
    StudentRepository studentRepo;

    private final
    AssignmentRepository asRepo;

    private static final int PAGE_SIZE = 15;
    private static final int INITAL_PAGE = 0;
    private Log log = LogFactory.getLog(this.getClass());


    @Autowired
    public StudentController(UserRepository userRepo, StudentRepository studentRepo, AssignmentRepository asRepo) {
        this.userRepo = userRepo;
        this.studentRepo = studentRepo;
        this.asRepo = asRepo;
    }

    @GetMapping("/createstudentprofile")
    public String getStudentProfileForm() {
        return "student/createProfileStudent";
    }

    @PostMapping("/createstudentprofile")
    public String createUser(Principal principal, @RequestParam("gsm") String gsm,
                             @RequestParam(value = "zap", required = false) String zap,
                             @RequestParam(value = "mobility", required = false) String mobility) {

        User currentUser = userRepo.findByEmail(principal.getName());
        Student student = new Student(gsm, currentUser.getEmail().substring(1, 7), currentUser);

        boolean existsStudent = studentRepo.existsByStudentNumber(currentUser.getEmail().substring(1, 7));

        if (!existsStudent) {
            if (zap != null) {
                student.setZap(true);
            }
            if (mobility != null) {
                student.setMobility(true);
            }
            currentUser.setFirstLogin(false);
            userRepo.save(currentUser);
            studentRepo.save(student);
        }

        return "redirect:/main";
    }

    @GetMapping("/liststudents")
    public String listStudents(Model model, Optional<Integer> page, Principal principal) {
        int buttons = (int) studentRepo.count() / PAGE_SIZE;
        if (studentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        Page<Student> students = studentRepo.findAll(PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(students.getTotalPages(), students.getNumber(), buttons);

        model.addAttribute("pager", pager);
        model.addAttribute("persons", students);
        model.addAttribute("students", students);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "student/listStudents";
    }

    //search in liststudentss
    @GetMapping("/liststudents/search/{searchbar}")
    public String searchByStudentNrOrName(@PathVariable("searchbar") String name, Model model, Optional<Integer> page, Principal principal) {
        Page students = null;

        int buttons = (int) studentRepo.count() / PAGE_SIZE;
        if (studentRepo.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        try {
            int studentenNummer = Integer.parseInt(name);
            students = studentRepo.findByStudentNumberContainingOrderByStudentId(name, PageRequest.of(evalPage, PAGE_SIZE));

        } catch (Exception e) {
            List<User> users = userRepo.findByFirstnameContainingOrLastnameContaining(name, name);
            List<Long> usersId = new ArrayList<>();
            for (User item : users) {
                usersId.add(item.getUserId());
            }
            if (usersId.size() != 0) {
                students = studentRepo.findByUserids(usersId, PageRequest.of(evalPage, PAGE_SIZE));

            } else {
                usersId.add((long) 0);
                students = studentRepo.findByUserids(usersId, PageRequest.of(evalPage, PAGE_SIZE));
            }

        }

        Pager pager = new Pager(students.getTotalPages(), students.getNumber(), buttons);
        model.addAttribute("students", students);
        model.addAttribute("persons", students);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "student/listStudents";
    }

    @GetMapping("/studentProfile")
    public String getProfileOfCurrentStudent(Model model, Principal principal) {
        Student currentStudent = studentRepo.findByUserId(userRepo.findByEmail(principal.getName()));
        List<Assignment> assignments = asRepo.findByAssignerUserId(currentStudent.getUserId());
        model.addAttribute("student", currentStudent);
        model.addAttribute("assignments", assignments);

        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "student/studentProfile";
    }

    @PostMapping("/studentProfile")
    public String updateStudentProfile(Principal principal, @RequestParam("Gsm") String
            gsm, @RequestParam("type") String type) {
        Student currentStudent = studentRepo.findByUserId(userRepo.findByEmail(principal.getName()));
        if (type.equalsIgnoreCase("zap en mobility")) {
            currentStudent.setZap(true);
            currentStudent.setMobility(true);
        } else if (type.equalsIgnoreCase("zap")) {
            currentStudent.setZap(true);
            currentStudent.setMobility(false);
        } else if (type.equalsIgnoreCase("mobility")) {
            currentStudent.setZap(false);
            currentStudent.setMobility(true);
        } else {
            currentStudent.setZap(currentStudent.isZap());
            currentStudent.setMobility(currentStudent.isMobility());
        }
        currentStudent.setGsm(gsm);
        studentRepo.save(currentStudent);
        return "redirect:studentProfile";
    }


}
