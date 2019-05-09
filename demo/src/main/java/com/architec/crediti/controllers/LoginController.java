package com.architec.crediti.controllers;

import com.architec.crediti.models.ExternalUser;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.ExternalUserRepository;
import com.architec.crediti.repositories.HashPass;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    ExternalUserRepository exRepo;

    @Autowired
    UserRepository userRepo;

    @RequestMapping("/login")
    public String getLogin() {
        return "login";
    }

    @RequestMapping("/loginError")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @PostMapping("/login")
    public String login( Model model,
                         @RequestParam("password") String password,
                         @RequestParam("username") String username){

        // If the email exists in 'user' table, get the id of that user, search for
        // that id in the 'extern' table and grab the password hash
        if (userRepo.findByEmail(username) != null) {
            User user = userRepo.findByEmail(username);

            if (exRepo.findByUserId(user) != null ) {
                ExternalUser exUser = exRepo.findByUserId(user);

                // Make sure extern is not null
                String hash = HashPass.convertToPbkdf2(password.toCharArray(), exUser.getSalt());

                if(hash.equals(exUser.getPassword())){
                    return "redirect:/listAllAssignments";
                }
            }
        }
        model.addAttribute("status", "email of password is niet juist");
        return "login";
    }
}
