package com.architec.crediti.controllers;

import com.architec.crediti.email.EmailServiceImpl;
import com.architec.crediti.email.EmailTemplates;
import com.architec.crediti.models.Assignment;
import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.Pager;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.security.HashPass;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class ExternalController {

    private final ExternalUserRepository externalUserRepository;

    private final
    UserRepository userRepository;

    private final
    EmailServiceImpl mail;

    private final
    AssignmentRepository asRepo;

    private static final int PAGE_SIZE = 2;
    private static final int INITAL_PAGE = 0;

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
        ExternalUser externalUser = new ExternalUser(firstname, lastname, company, phone, address, city, postal, hashedBytes[0].toString().toCharArray(), (byte[]) hashedBytes[1]);
        // create a internal user
        User user = new User(firstname, lastname, email, Role.EXTERN, false);
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            // set the foreign key
            externalUser.setUserId(user);
            externalUserRepository.save(externalUser);
            long userId = userRepository.findByEmail(email).getUserId();

            userRepository.save(user);
            externalUserRepository.save(externalUser);

            String name = firstname + " " + lastname;
            String fullAddress = address + ", " + postal + " " + city;

            List<User> coordinators = userRepository.findAllByRole(Role.COORDINATOR);
            for (User u : coordinators) {
                mail.sendSimpleMessage(u.getEmail(), "Nieuwe externe registratie",
                        EmailTemplates.newExternalUser(name, company, fullAddress, phone, email));
            }

            mail.sendSimpleMessage(externalUser.getEmail(), "Registratie", EmailTemplates.newExternal());
        } else {
            mail.sendSimpleMessage(email, "Externe registratie geannuleerd",
                    EmailTemplates.userAlreadyExists());
            return "redirect:/registerfail";
        }
        return "redirect:/registersucces";
    }


    @GetMapping("/registersucces")
    public String getSucces() {
        return "external/registerSucces";
    }

    @GetMapping("/registerfail")
    public String getFail() {
        return "external/registerFail";
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
        model.addAttribute("name", user.getFirstname() + " " + user.getLastname().substring(0, 1) + ".");
        return "external/externalUserProfile";
    }

    @PostMapping("/externalUserProfile")
    public String changeExtUser(Principal principal,
                                @RequestParam("lastname") String lastname,
                                @RequestParam("firstname") String firstname,
                                @RequestParam("company") String company,
                                @RequestParam("phone") String phone,
                                @RequestParam("address") String address,
                                @RequestParam("city") String city,
                                @RequestParam("postal") String postal) {


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
            if (u != null && !externalUserRepository.findByUserId(u).isApproved()) {
                externalUsers.add(externalUserRepository.findByUserId(u));
            }
        }

        model.addAttribute("extern", externalUsers);
        model.addAttribute("users", users);
        //pass username to header fragment
        User currentUser = userRepository.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "external/listUnvalidatedExternal";
    }

    @GetMapping("/validateexternal/{externalId}")
    public String validateExternal(@PathVariable("externalId") int externalId) {
        ExternalUser extUser = externalUserRepository.findByUserId(userRepository.findByUserId(externalId));
        extUser.setApproved(true);
        externalUserRepository.save(extUser);

        mail.sendSimpleMessage(userRepository.findByUserId(externalId).getEmail(), "Externe registratie gevalideerd",
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

    @GetMapping("/listexternal")
    public String listExternal(Model model, Optional<Integer> page, Principal principal){
        int buttons = (int) externalUserRepository.count() / PAGE_SIZE;
        if (externalUserRepository.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        Page<ExternalUser> external = externalUserRepository.findByApproved(true, PageRequest.of(evalPage, PAGE_SIZE));
        Pager pager = new Pager(external.getTotalPages(), external.getNumber(), buttons);

        model.addAttribute("pager", pager);
        model.addAttribute("persons", external);
        model.addAttribute("external", external);
        model.addAttribute("selectedPageSize", PAGE_SIZE);

        //pass username to header fragment
        User currentUser = userRepository.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "external/listexternal";
    }

    @GetMapping("/listexternal/search/{searchbar}")
    public String searchByStudentNrOrName(@PathVariable("searchbar") String name, Model model, Optional<Integer> page, Principal principal) {
        Page external = null;

        int buttons = (int) externalUserRepository.count() / PAGE_SIZE;
        if (externalUserRepository.count() % PAGE_SIZE != 0) {
            buttons++;
        }
        int evalPage = (page.orElse(0) < 1) ? INITAL_PAGE : page.get() - 1;

        try {
            //DEZE ONGEBRUIKTE CODE MAG NIET WEG!!
            int studentenNummer = Integer.parseInt(name);
            external = externalUserRepository.findByUserIdContainingOrderByUserId(name, PageRequest.of(evalPage, PAGE_SIZE));

        } catch (Exception e) {
            List<User> users = externalUserRepository.findByFirstnameContainingOrLastnameContaining(name, name);
            List<Long> usersId = new ArrayList<>();
            for (User item : users) {
                usersId.add(item.getUserId());
            }
            if (!usersId.isEmpty()) {

                external = externalUserRepository.findByUserids(usersId, PageRequest.of(evalPage, PAGE_SIZE));

            } else {
                usersId.add((long) 0);
                external = externalUserRepository.findByUserids(usersId, PageRequest.of(evalPage, PAGE_SIZE));
            }

        }

        Pager pager = new Pager(external.getTotalPages(), external.getNumber(), buttons);
        model.addAttribute("students", external);
        model.addAttribute("persons", external);
        model.addAttribute("selectedPageSize", PAGE_SIZE);
        model.addAttribute("pager", pager);
        //pass username to header fragment
        User currentUser = userRepository.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "external/listexternal";
    }
}
