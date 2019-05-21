package com.architec.crediti.controllers;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.HashPass;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ExternalController {

    private final ExternalUserRepository externalUserRepository;

    private final
    UserRepository userRepository;

    private final
    EmailServiceImpl mail;

    @Autowired
    public ExternalController(ExternalUserRepository externalUserRepository, UserRepository userRepository, EmailServiceImpl mail) {
        this.externalUserRepository = externalUserRepository;
        this.userRepository = userRepository;
        this.mail = mail;
    }

    @GetMapping("/createexternaluser")
    public String getCreateUser() {

        return "createExternal";
    }

    @PostMapping("/createexternal")
    public String createUser(Model model,
                            @RequestParam("lastname") String lastname, @RequestParam("firstname") String firstname,
                             @RequestParam("company") String company, @RequestParam("address") String address,
                             @RequestParam("postal") String postal, @RequestParam("city") String city, @RequestParam("phone") String phone,
                             @RequestParam("email") String email, @RequestParam("password") String password) {


        Object[] hashedBytes = HashPass.convertToPbkdf2(password.toCharArray());
        // create an external user
        ExternalUser externalUser = new ExternalUser(firstname, lastname, company, phone, address, city, postal, hashedBytes[0].toString().toCharArray(),(byte[]) hashedBytes[1]);
        // create a internal user
        User user = new User(firstname, lastname, email, Role.EXTERNE,false);
        // set the foreign key
        externalUser.setUserId(user);

        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            externalUserRepository.save(externalUser);

            long userId = userRepository.findByEmail(email).getUserId();
            String name = firstname + " " + lastname;
            String fullAddress = address + ", " + postal + " " + city;
            //TODO: vervang 's097086@ap.be' door mail van coordinator
            mail.sendSimpleMessage("s100605@ap.be", "Nieuwe externe registratie",
            EmailTemplates.newExternalUser(userId, name, company, fullAddress, phone, email));
        }
        else{
            model.addAttribute("error" , "Email bestaal al");
            return "createExternal";
        }

        return "redirect:/registersucces";
    }


    @GetMapping("/registersucces")
    public String getSucces() {
        return "registerSucces";
    }

    @GetMapping("/notapproved")
    public String notApproved() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.setAuthenticated(false);
        SecurityContextHolder.clearContext();
        return "notapproved";
    }


    @GetMapping("/externalUserProfile")
    public String externalUsers(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        ExternalUser externalUser = externalUserRepository.findByUserId(user);

        model.addAttribute("externalUsers", externalUser);
        return "externalUserProfile";
    }

    @PostMapping("/externalUserProfile")
    public String changeExtUser(Principal principal,
                                @RequestParam ("lastname") String lastname,
                                @RequestParam ("firstname") String firstname,
                                @RequestParam ("company") String company,
                                @RequestParam ("phone") String phone,
                                @RequestParam ("address") String address,
                                @RequestParam ("city") String city,
                                @RequestParam ("postal") String postal,
                                @RequestParam ("password") String password) {


        User user = userRepository.findByEmail(principal.getName());
        ExternalUser externalUser = externalUserRepository.findByUserId(user);
        externalUser.setLastname(lastname);
        externalUser.setFirstname(firstname);
        externalUser.setCompany(company);
        externalUser.setPhone(phone);
        externalUser.setAddress(address);
        externalUser.setCity(city);
        externalUser.setPostal(postal);

        Object[] hashBytes = HashPass.convertToPbkdf2(password.toCharArray());
        externalUser.setPassword(hashBytes[0].toString().toCharArray());
        externalUser.setSalt((byte[]) hashBytes[1]);

        externalUserRepository.save(externalUser);

        return "redirect:/externalUserProfile";

    }

    @GetMapping("/listUnvalidatedExternal")
    public String listUnvalidatedExternal(Model model) {
        List<User> users = userRepository.findAllByRole(Role.EXTERNE);
        List<ExternalUser> externalUsers = new ArrayList<>();
        for (User u : users) {
            if (!externalUserRepository.findByUserId(u).isApproved()) {
                externalUsers.add(externalUserRepository.findByUserId(u));
            }
        }

        model.addAttribute("externe", externalUsers);
        model.addAttribute("users", users);
        return "listUnvalidatedExternal";
    }

    @GetMapping("/validateexternal/{externalId}")
    public String validateExternal(@PathVariable("externalId") int externalId) {
        ExternalUser extUser = externalUserRepository.findByUserId(userRepository.findByUserId(externalId));
        extUser.setApproved(true);
        externalUserRepository.save(extUser);

        mail.sendSimpleMessage(userRepository.findByUserId(externalId).getEmail(), "externe gevalideerd",
                EmailTemplates.validatedExternal());

        return "redirect:/listUnvalidatedExternal";
    }

    @GetMapping("/deleteexternal/{externalId}")
    public String deleteExternal(@PathVariable("externalId") int externalId) {
        ExternalUser extUser = externalUserRepository.findByUserId(userRepository.findByUserId(externalId));

        mail.sendSimpleMessage(userRepository.findByUserId(externalId).getEmail(), "externe niet gevalideerd",
                EmailTemplates.notValidatedExternal());

        externalUserRepository.delete(extUser);
        return "redirect:/listUnvalidatedExternal";
    }

}
