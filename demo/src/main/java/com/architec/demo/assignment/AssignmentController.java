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

    private List<Assignment> assignments;

    @RequestMapping(value = "/assignments", method = RequestMethod.GET)
    public String assignments(Model model) /*throws SQLException*/ {

        assignments = assignmentRepository.findAll();
        System.out.println(assignments);
        System.out.println(assignments.get(0).getTitle());
//        String test = assignments.get(0).getTitle();
//
//        model.addAttribute("titel", test);
//        model.addAttribute("all", assignments);
//        Connection c = getConnectionToDb();
//        String result = getAssignment(c);
//        model.addAttribute("assign", result);
//        System.out.println(result);
        return "assignments";
    }

    @PostMapping("/assignments")
    public Assignment createAssignment(@Valid @RequestBody Assignment assignemnt) {
        return assignmentRepository.save(assignemnt);
    }

//    public String getAssignment(Connection con) throws SQLException {
//        String result = "";
//        String query = "select * from assignment";
//
//        PreparedStatement prm = con.prepareStatement(query);
//        ResultSet set = prm.executeQuery();
//
//        while (set.next()) {
//            result = result + set.getString("title") + " ";
//            result = result + set.getString("type") + " ";
//        }
//
//        return result;
//    }
//
//    public Connection getConnectionToDb() {
//        try {
//            Connection c = DriverManager.getConnection("jdbc:sqlserver://192.168.84.92;databaseName=crediti;username=sa;password=Zxcvb0123");
//            System.out.println("connected");
//
//            return c;
//
//        } catch (Exception ex) {
//            System.out.println(ex);
//            return null;
//        }
//    }
}
