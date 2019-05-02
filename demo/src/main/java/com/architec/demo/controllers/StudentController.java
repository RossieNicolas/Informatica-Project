package com.architec.demo.controllers;

import com.architec.demo.models.Student;
import com.architec.demo.models.User;
import com.architec.demo.repositories.UserRepository;
import com.architec.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentController {

    @Autowired
    StudentRepository studentRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/createstudentprofile")
    public String getStudentProfileForm() {
        return "createProfileStudent";
    }

    @PostMapping("/createstudentprofile")
    public String createUser(@RequestParam("student_id") String studentId, @RequestParam("firstName") String firstname,
            @RequestParam("lastname") String lastname, @RequestParam("email") String email,
            @RequestParam("gsm") String gsm, @RequestParam("class") String s_class) {

        // create an external user
        Student externalUser = new Student(studentId, s_class, gsm);
        // create a internal user
        User user = new User(firstname, lastname, email, "Student");
        // set the foreign key
        externalUser.setUserId(user);

        if (!userRepo.existsByEmail(user.getEmail())) {
            userRepo.save(user);
            studentRepo.save(externalUser);
        }

        /*
         * If the person is already registered, he'll get a passwordrecovery mail, if
         * the person isn't registered yet, he'll get a comfirmation mail
         */
        return "redirect:/registersucces";
    }

}