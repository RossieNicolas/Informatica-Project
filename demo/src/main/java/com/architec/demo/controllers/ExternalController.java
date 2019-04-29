package com.architec.demo.controllers;

import javax.validation.Valid;

import com.architec.demo.models.ExternalUser;
import com.architec.demo.models.User;
import com.architec.demo.repositories.ExternalUserRepository;
import com.architec.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExternalController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    ExternalUserRepository externalUserRepo;

    @GetMapping("/createexternaluser")
    public String getCreateUser() {

        return "createExternal";
    }

    @PostMapping("/createexternal")
    public String createUser(@RequestParam("name") String lastname, @RequestParam("firstName") String firstname,
            @RequestParam("company") String company, @RequestParam("adress") String adress,
            @RequestParam("postal") String postalcode, @RequestParam("city") String city,
            @RequestParam("mail") String mail, @RequestParam("password") String password) {

        ExternalUser externalUser = new ExternalUser(company, adress, postalcode, city, password);
        User user = new User(firstname, lastname, mail, "Externe opdrachtgever");
        externalUser.setUserId(user);

        System.out.println(externalUser);

        if (!userRepo.existsByEmail(user.getEmail())) {
            userRepo.save(user);
            externalUserRepo.save(externalUser);
        }

        return "redirect:/login";
    }
}