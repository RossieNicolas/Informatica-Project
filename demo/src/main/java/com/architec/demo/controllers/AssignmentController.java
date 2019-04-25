package com.architec.demo.controllers;

import com.architec.demo.models.Assignment;
import com.architec.demo.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @RequestMapping(value = "/myassignments", method = RequestMethod.GET)
    public String assignments(Model model) /*throws SQLException*/ {

        List<Assignment> assignments = assignmentRepository.findAll();
        System.out.println(assignments);

        model.addAttribute("assignments", assignments);
        return "myassignments";
    }

//    @PostMapping("/myassignments")
//    public Assignment createAssignment(@Valid @RequestBody Assignment assignemnt) {
//        return assignmentRepository.save(assignemnt);
//    }
}
