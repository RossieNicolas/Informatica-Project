package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.repositories.AssignmentMethods;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    private final
    UserRepository userRepo;

    private final
    StudentRepository studentRepo;

    private final
    AssignmentRepository asRepo;


    @Autowired
    public StudentController(UserRepository userRepo, StudentRepository studentRepo , AssignmentRepository asRepo) {
        this.userRepo = userRepo;
        this.studentRepo = studentRepo;
        this.asRepo = asRepo;
    }

    @GetMapping("/createstudentprofile")
    public String getStudentProfileForm() {
        return "createProfileStudent";
    }

    @PostMapping("/createstudentprofile")
    public String createUser(Principal principal, @RequestParam("gsm") String gsm,
                             @RequestParam(value = "zap", required = false) String zap,
                             @RequestParam(value = "mobility", required = false) String mobility) {

        User currentUser = userRepo.findByEmail(principal.getName());
        Student student = new Student(gsm, currentUser.getEmail().substring(1,7), currentUser);

        boolean existsStudent = studentRepo.existsByStudentennummer(currentUser.getEmail().substring(1,7));

        if (!existsStudent) {
            if (zap != null) {student.setZap(true); }
            if (mobility != null) {student.setMobility(true); }
            currentUser.setFirstLogin(false);
            userRepo.save(currentUser);
            studentRepo.save(student);
        }

        return "redirect:/main";
    }
    @GetMapping("/liststudents")
    public String listStudents(Model model){
            model.addAttribute("students", studentRepo.findAll());
            return "listStudents";
    }

    @PostMapping("/liststudents")
    String getStudents(@RequestParam("searchbar") String name, Model model) {
        List<Student> students = new ArrayList();

        try {
            Student st = studentRepo.findByStudentennummer(name);
            model.addAttribute("students", st);
        } catch (Exception e) {
            if(!name.equals("")){
                for (User item : userRepo.findByFirstnameContainingOrLastnameContaining(name, name)) {
                    students.add(studentRepo.findByUserId(item));
                }
                model.addAttribute("students", students);
            }
            else{
                model.addAttribute("students", studentRepo.findAll());
            }
        }

        return "listStudents";
    }

    @GetMapping("/studentProfile")
    String getProfileOfCurrentStudent(Model model, Principal principal){
        Student currentStudent = studentRepo.findByUserId(userRepo.findByEmail(principal.getName()));
        List<Assignment> assignments = asRepo.findByAssignerUserId(currentStudent.getUserId());
        model.addAttribute("student", currentStudent);
        model.addAttribute("assignments", assignments);
        return "studentProfile";
    }



}
