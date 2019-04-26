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
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/myassignments", method = RequestMethod.GET)
    public String assignments(Model model) {

        //List<Assignment> myAssignments = assignmentRepository.findByAssignerUserId();
        List<Assignment> assignments = assignmentRepository.findAll();

        model.addAttribute("assignments", assignments);
        //model.addAttribute("myAssignments", myAssignments);
        return "myassignments";
    }

//    @PostMapping("/myassignments")
//    public Assignment createAssignment(@Valid @RequestBody Assignment assignemnt) {
//        return assignmentRepository.save(assignemnt);
//    }
}
