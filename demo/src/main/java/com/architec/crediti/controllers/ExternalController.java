package com.architec.crediti.controllers;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.security.HashPass;
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

    private final
    AssignmentRepository asRepo;

    @Autowired
    public ExternalController(ExternalUserRepository externalUserRepository, UserRepository userRepository, EmailServiceImpl mail, AssignmentRepository asRepo) {
        this.externalUserRepository = externalUserRepository;
        this.userRepository = userRepository;
        this.mail = mail;
        this.asRepo = asRepo;
    }

    @GetMapping("/createexternaluser")
    public String getCreateUser() {
        return "external/createExternal";
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
        User user = new User(firstname, lastname, email, Role.EXTERN,false);
        // set the foreign key
        externalUser.setUserId(user);
        long userId = userRepository.findByEmail(email).getUserId();

        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            externalUserRepository.save(externalUser);

            String name = firstname + " " + lastname;
            String fullAddress = address + ", " + postal + " " + city;
            //TODO: vervang 's097086@ap.be' door mail van coordinator
            mail.sendSimpleMessage("s100605@ap.be", "Nieuwe externe registratie",
            EmailTemplates.newExternalUser(userId, name, company, fullAddress, phone, email));

            mail.sendSimpleMessage(externalUser.getEmail(),"Registratie", EmailTemplates.newExternal());
        }
        else{
            model.addAttribute("error" , true);
            mail.sendSimpleMessage(userRepository.findByUserId(userId).getEmail(), "Externe registratie geannuleerd",
                    EmailTemplates.userAlreadyExists());
            return "external/createExternal";

        }

        return "redirect:/registersucces";
    }


    @GetMapping("/registersucces")
    public String getSucces() {
        return "external/registerSucces";
    }

    @GetMapping("/notapproved")
    public String notApproved() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.setAuthenticated(false);
        SecurityContextHolder.clearContext();
        return "external/notapproved";
    }


    @GetMapping("/externalUserProfile")
    public String externalUsers(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        ExternalUser externalUser = externalUserRepository.findByUserId(user);
        List<Assignment> assignments = asRepo.findByAssignerUserId(externalUser.getUserId());
        model.addAttribute("externalUsers", externalUser);
        model.addAttribute("assignments", assignments);
        //pass username to header fragment
        model.addAttribute("name",user.getFirstname() + " " + user.getLastname().substring(0,1) + ".");
        return "external/externalUserProfile";
    }

    @PostMapping("/externalUserProfile")
    public String changeExtUser(Principal principal,
                                @RequestParam ("lastname") String lastname,
                                @RequestParam ("firstname") String firstname,
                                @RequestParam ("company") String company,
                                @RequestParam ("phone") String phone,
                                @RequestParam ("address") String address,
                                @RequestParam ("city") String city,
                                @RequestParam ("postal") String postal) {


        User user = userRepository.findByEmail(principal.getName());
        ExternalUser externalUser = externalUserRepository.findByUserId(user);
        externalUser.setLastname(lastname);
        externalUser.setFirstname(firstname);
        externalUser.setCompany(company);
        externalUser.setPhone(phone);
        externalUser.setAddress(address);
        externalUser.setCity(city);
        externalUser.setPostal(postal);

        externalUserRepository.save(externalUser);

        return "redirect:/externalUserProfile";
    }

    @GetMapping("/listUnvalidatedExternal")
    public String listUnvalidatedExternal(Principal principal, Model model) {
        List<User> users = userRepository.findAllByRole(Role.EXTERN);
        List<ExternalUser> externalUsers = new ArrayList<>();
        for (User u : users) {
            if (!externalUserRepository.findByUserId(u).isApproved()) {
                externalUsers.add(externalUserRepository.findByUserId(u));
            }
        }

        model.addAttribute("extern", externalUsers);
        model.addAttribute("users", users);
        //pass username to header fragment
        User currentUser = userRepository.findByEmail(principal.getName());
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "external/listUnvalidatedExternal";
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
