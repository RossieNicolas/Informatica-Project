package com.architec.crediti.controllers;

import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.StudentRepository;
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
    public String createUser(@RequestParam("student_id") String studentId, @RequestParam("firstname") String firstname,
                             @RequestParam("lastname") String lastname, @RequestParam("mail") String email,
                             @RequestParam("gsm") String gsm, @RequestParam("class") String s_class) {

        Student student = new Student(studentId, s_class, gsm);
        User user = new User(firstname, lastname, email, "Student");
        // set the foreign key
        student.setUserId(user);

        if (!userRepo.existsByEmail(user.getEmail())) {
            userRepo.save(user);
            studentRepo.save(student);
        }

        /*
         * If the person is already registered, he'll get a passwordrecovery mail, if
         * the person isn't registered yet, he'll get a comfirmation mail
         */
        return "redirect:/registersucces";
    }

}
