package com.architec.crediti.controllers;

import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.AssignmentRepository;
import com.architec.crediti.repositories.EnrolledRepository;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private final
    UserRepository userRepo;

    private final
    ExternalUserRepository exRepo;

    private final
    AssignmentRepository assignRepo;

    private final
    EnrolledRepository enrollRepo;

    @Autowired
    public MainController(UserRepository userRepo, ExternalUserRepository exRepo, AssignmentRepository assignRepo, EnrolledRepository enrollRepo) {
        this.userRepo = userRepo;
        this.exRepo = exRepo;
        this.assignRepo = assignRepo;
        this.enrollRepo = enrollRepo;
    }

    @GetMapping("/main")
    public String getDashboard(Principal principal, Model model) {
        User currentUser = userRepo.findByEmail(principal.getName());

        if (currentUser.isFirstLogin()) {
            return "redirect:/createstudentprofile";
        }

        if (exRepo.findByUserId(currentUser) != null && !exRepo.findByUserId(currentUser).isApproved()) {
            return "redirect:/loginUnapproved";

        }
        int unapprovedExternals = exRepo.countByApproved(false);
        int unvalidatedAssignments = assignRepo.countByValidated(false);
        int unapprovedEnrollments = enrollRepo.countAllByEnrolledIdNotNull();

        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        model.addAttribute("externals", unapprovedExternals);
        model.addAttribute("assignments", unvalidatedAssignments);
        model.addAttribute("enrolled", unapprovedEnrollments);
        return "basic/main";
    }
}
