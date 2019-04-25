package com.architec.demo.assignment;

import com.architec.demo.repositories.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @RequestMapping(value = "/assignments", method = RequestMethod.GET)
    public String assignments(Model model) /*throws SQLException*/ {

        List<Assignment> assignments = assignmentRepository.findAll();
        System.out.println(assignments);

        model.addAttribute("assignments", assignments);
        return "assignments";
    }

    @PostMapping("/assignments")
    public Assignment createAssignment(@Valid @RequestBody Assignment assignemnt) {
        return assignmentRepository.save(assignemnt);
    }
}
