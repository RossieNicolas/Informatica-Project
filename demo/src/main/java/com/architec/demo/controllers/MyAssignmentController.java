package com.architec.demo.controllers;

import com.architec.demo.models.Assignment;
import com.architec.demo.models.User;
import com.architec.demo.repositories.AssignmentRepository;
import com.architec.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MyAssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @RequestMapping(value = "/myassignments", method = RequestMethod.GET)
    public String assignments(Model model) {

        List<Assignment> assignments = assignmentRepository.findAll();
        List<Assignment> filter = assignmentRepository.findByMaxStudents(2);
        List<Assignment> users = assignmentRepository.findByAssignerUserId(1);

        model.addAttribute("assignments", assignments);
        model.addAttribute("filter", filter);
        model.addAttribute("users", users);
        //model.addAttribute("myAssignments", myAssignments);
        return "myassignments";
    }

//    @PostMapping("/myassignments")
//    public Assignment createAssignment(@Valid @RequestBody Assignment assignemnt) {
//        return assignmentRepository.save(assignemnt);
//    }
}
