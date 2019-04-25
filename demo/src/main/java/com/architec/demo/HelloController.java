package com.architec.demo;

import com.architec.demo.models.Tag;
import com.architec.demo.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.sql.Connection;
import java.sql.DriverManager;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    @Autowired
    TagRepo tagRepo;
    @RequestMapping("/tag")
    public String assignment() {
        getConnectionToDb();
        return "tag";
    }

    @PostMapping("/tag")
    public Tag createTag(@Valid Tag tag){
        return tagRepo.save(tag);
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