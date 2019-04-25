package com.architec.demo.controller;

import com.architec.demo.jpa.Assignment;
import com.architec.demo.jpa.AssignmentRepo;
import com.architec.demo.jpa.Tag;
import com.architec.demo.jpa.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.sql.*;
import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    TagRepo tagRepo;

    @Autowired
    AssignmentRepo assignmentRepo;

    @RequestMapping("/assignment")
    public String assignment() {
        return "assignment";
    }

    @RequestMapping(value = "/assignment", method = RequestMethod.GET)
    public String tag(Model model) /*throws SQLException*/ {

        List<Tag> updatetag = tagRepo.findAll();


        model.addAttribute("updatetag", updatetag);
        return "assignment";
    }


    @PostMapping("/assignment")
    public Assignment createAssignment(@Valid Assignment assignment) {
        return assignmentRepo.save(assignment);
    }

}