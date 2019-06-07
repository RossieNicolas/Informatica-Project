package com.architec.crediti.controllers;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class CoordinatorController {

    private final UserRepository userRepo;

    @Autowired
    public CoordinatorController(UserRepository userRepository){
        this.userRepo = userRepository;
    }

    @GetMapping("/coordinator")
    public String getCoordinator(Model model, Principal principal) {
        List<User> list = userRepo.findAllByRole(Role.COORDINATOR);
        model.addAttribute("list", list);

        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "coordinator/coordinator";
    }

    @GetMapping("/addcoordinator")
    public String getAddCoordinator(Model model, Principal principal) {
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "coordinator/addCoordinator";
    }

    @PostMapping("/addcoordinator")
    public String addCoordinator(@RequestParam("email") String email) {
        User usr = userRepo.findByEmail(email);
        usr.setRole(Role.COORDINATOR);
        userRepo.save(usr);
        return "redirect:/coordinator";
    }

    @GetMapping("/deletecoordinator/{id}")
    public String deleteCoordiantor(@PathVariable("id") int id, Principal principal){
        User usr = userRepo.findByUserId((long) id);
        User current = userRepo.findByEmail(principal.getName());

        if(usr != null) {
            switch (usr.getEmail().substring(0, 1).toLowerCase()) {
                case "s":
                    usr.setRole(Role.STUDENT);
                    break;
                case "p":
                    usr.setRole(Role.DOCENT);
                    break;
                default:
                    usr.setRole(Role.EXTERN);
                    break;
            }

            userRepo.save(usr);
        }

        String output = "redirect:/coordinator";
        if(current.getEmail().equals(usr.getEmail())){
            output = "redirect:/login";
        }

        return output;
    }
}
