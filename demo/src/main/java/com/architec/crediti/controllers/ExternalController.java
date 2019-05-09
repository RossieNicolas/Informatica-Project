package com.architec.crediti.controllers;

import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.UserRepository;

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

        // // create an external user
        // ExternalUser externalUser = new ExternalUser(company, adress, postalcode,
        // city, password);
        // // create a internal user
        // User user = new User(firstname, lastname, mail, "Externe opdrachtgever",
        // false);
        // // set the foreign key
        // externalUser.setUserId(user);
        //
        // if (!userRepo.existsByEmail(user.getEmail())) {
        // userRepo.save(user);
        // externalUserRepo.save(externalUser);
        // }

        /*
         * If the person is already registered, he'll get a passwordrecovery mail, if
         * the person isn't registered yet, he'll get a comfirmation mail
         */
        return "redirect:/registersucces";
    }

    @GetMapping("/registersucces")
    public String getSucces() {
        return "registerSucces";
    }
}