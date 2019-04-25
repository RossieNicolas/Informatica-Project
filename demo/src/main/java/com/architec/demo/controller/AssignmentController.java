package com.architec.demo.controller;

import com.architec.demo.jpa.Assignment;
import com.architec.demo.jpa.AssignmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.sql.*;

@Controller
public class AssignmentController {

    @Autowired
    AssignmentRepo assignmentRepo;

    @RequestMapping("/")
    public String index() {
        return "Greetings from SpringBoot";
    }

    @RequestMapping("/assignment")
    public String assignment() {
        getConnectionToDb();
        return "assignment";
    }

    @PostMapping("/assignment")
    public Assignment createAssignment(@Valid Assignment assignment){
        return assignmentRepo.save(assignment);
    }

    public Connection getConnectionToDb() {
        try {
            Connection c = DriverManager.getConnection("jdbc:sqlserver://192.168.84.92;databaseName=crediti;username=sa;password=Zxcvb0123");
            System.out.println("connected");

            return c;

        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

}