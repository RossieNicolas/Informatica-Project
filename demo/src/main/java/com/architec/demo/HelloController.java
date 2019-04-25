package com.architec.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.*;

@Controller
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from SpringBoot";
    }

    @RequestMapping("/assignment")
    public String test() {
        getConnectionToDb();
        return "CreateAssignmentForm";
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