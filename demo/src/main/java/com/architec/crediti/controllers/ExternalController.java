package com.architec.crediti.controllers;

import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.HashPass;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ExternalController {

    @Autowired
    private ExternalUserRepository externalUserRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/createexternaluser")
    public String getCreateUser() {

        return "createExternal";
    }

    @PostMapping("/createexternal")
    public String createUser(@RequestParam("lastname") String lastname, @RequestParam("firstname") String firstname,
                             @RequestParam("company") String company, @RequestParam("address") String address,
                             @RequestParam("postal") String postal, @RequestParam("city") String city, @RequestParam("phone") String phone,
                             @RequestParam("email") String email, @RequestParam("password") String password) {


        Object[] hashedBytes = HashPass.convertToPbkdf2(password.toCharArray());
        // create an external user
        ExternalUser externalUser = new ExternalUser(firstname, lastname, company, phone, address, city, postal, hashedBytes[0].toString().toCharArray(),(byte[]) hashedBytes[1]);
        // create a internal user
        User user = new User(firstname, lastname, email, "Externe",false);
        // set the foreign key
        externalUser.setUserId(user);

        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            externalUserRepository.save(externalUser);
            //TODO: stuur mail naar coordinator/externe
        }

        return "redirect:/registersucces";
    }


    @GetMapping("/registersucces")
    public String getSucces() {
        return "registerSucces";
    }

    @RequestMapping("/notapproved")
    public String notApproved() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.setAuthenticated(false);
        SecurityContextHolder.clearContext();
        return "notapproved";
    }


    @RequestMapping(value = "/externalUserProfile", method = RequestMethod.GET)
    public String externalUsers(Principal principal, Model model) {
        User user = userRepository.findByEmail(principal.getName());
        ExternalUser externalUser = externalUserRepository.findByUserId(user);

        model.addAttribute("externalUsers", externalUser);
        return "externalUserProfile";
    }

    @PostMapping("/externalUserProfile")
    public String changeExtUser(Principal principal,
                                @Valid ExternalUser externalUser,
                                @RequestParam ("lastname") String lastname,
                                @RequestParam ("firstname") String firstname,
                                @RequestParam ("company") String company,
                                @RequestParam ("phone") String phone,
                                @RequestParam ("address") String address,
                                @RequestParam ("city") String city,
                                @RequestParam ("postal") String postal,
                                @RequestParam ("password") String password) {


        User user = userRepository.findByEmail(principal.getName());
        externalUser = externalUserRepository.findByUserId(user);
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

    @RequestMapping(value = "/listUnvalidatedExternal", method = RequestMethod.GET)
    public String listUnvalidatedExternal(Model model) {
        List<User> users = userRepository.findAllByRole("Externe");
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
        return "redirect:/listUnvalidatedExternal";
    }

    @GetMapping("/deleteexternal/{externalId}")
    public String deleteExternal(@PathVariable("externalId") int externalId) {
        ExternalUser extUser = externalUserRepository.findByUserId(userRepository.findByUserId(externalId));
        //TODO: e-mail naar externe dat die niet gevalideerd werd.

        externalUserRepository.delete(extUser);
        return "redirect:/listUnvalidatedExternal";
    }

}
