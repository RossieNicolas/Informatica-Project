package com.architec.crediti.controllers;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordRecoveryController {
    private final
    UserRepository userRepo;

    @Autowired
    public PasswordRecoveryController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/passwordRecovery")
    public String getPassword() {
        return "passwordRecovery";
    }

    @PostMapping("/passwordRecovery")
    public String confirmUserAccount(Model model , @RequestParam("email") String email) {

        boolean check = false;
        for (User u : userRepo.findAll()) {
            System.out.println(u.getEmail());
            if (u.getEmail().equals(email.toLowerCase())) {
                check = true;

            }
        }

        if(check){
            model.addAttribute("succes", "U hebt met succes een wachtwoordherstel aangevraagd. Er is een email verstuurd naar het opgegeven emailadres.");
        }
        else{
            model.addAttribute("fail", "Het gegeven emailadres werd niet gevonden.");
        }

        return "verificationSucces";
    }
}
