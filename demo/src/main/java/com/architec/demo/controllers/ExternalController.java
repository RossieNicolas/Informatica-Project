package com.architec.demo.controllers;

import javax.validation.Valid;

import com.architec.demo.models.ExternalUser;
import com.architec.demo.repositories.ExternalUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ExternalController {
    @Autowired
    ExternalUserRepository userRepo;

    @GetMapping("/createexternaluser")
    public String getAllAssingments() {

        return "createExternal";
    }

    @PostMapping("/createexternal")
    public void createTag(@Valid ExternalUser externalUser) {

        userRepo.save(externalUser);
    }
}