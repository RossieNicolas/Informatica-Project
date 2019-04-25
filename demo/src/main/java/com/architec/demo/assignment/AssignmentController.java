package com.architec.demo.assignment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AssignmentController {
    private List<Assignment> assignments;

    public AssignmentController() {
        assignments = new ArrayList<>();

//        assignments.add(new Assignment(1, "opdracht 1", "soort 1", "omschriving opdracht 1",
//                5, 2, "01/01/2019", "02/02/2019",false, true, "extra info1", "opdrachtgever"));
//
//        assignments.add(new Assignment(2, "opdracht 2", "soort 2", "omschriving opdracht 2",
//                5, 2, "01/01/2019", "02/02/2019", "opdrachtgever"));
//
//        assignments.add(new Assignment(3, "opdracht 3", "soort 3", "omschriving opdracht 3",
//                5, 2, "01/01/2019", "02/02/2019", "opdrachtgever"));
    }

    @RequestMapping(value = "/assignments", method = RequestMethod.GET)
    public String assignments(Model model) throws SQLException {

//        String test = assignments.get(0).getTitle();
//
//        model.addAttribute("titel", test);
//        model.addAttribute("all", assignments);
        Connection c = getConnectionToDb();
        String result = getAssignment(c);
        model.addAttribute("assign", result);
        System.out.println(result);
        return "assignments";
    }

    public String getAssignment(Connection con) throws SQLException {
        String result = "";
        String query = "select * from assignment";

        PreparedStatement prm = con.prepareStatement(query);
        ResultSet set = prm.executeQuery();

        while (set.next()) {
            result = result + set.getString("title") + " ";
            result = result + set.getString("type") + " ";
        }

        return result;
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
