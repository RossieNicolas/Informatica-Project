package com.architec.crediti.controllers;

import com.architec.crediti.models.*;
import com.architec.crediti.repositories.DocumentationRepository;
import com.architec.crediti.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.architec.crediti.repositories.FileRepository;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.upload.FileStorageService;
import com.architec.crediti.utils.PortfolioUtil;

import java.security.Principal;
import java.util.*;

/*
Some functions from
https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example/blob/master/src/main/java/com/example/filedemo/controller/FileController.java
 */

@Controller
public class PortfolioController {
    private final PortfolioUtil portfolioUtil;

    private final FileStorageService fileStorageService;

    private final FileRepository fileRepo;

    private final UserRepository userRepo;

    private final DocumentationRepository docRepo;

    private final StudentRepository studentRepo;

    @Autowired
    public PortfolioController(PortfolioUtil portfolioUtil, FileStorageService fileStorageService, FileRepository fileRepo, UserRepository userRepo, DocumentationRepository docRepo, StudentRepository studRepo) {
        this.portfolioUtil = portfolioUtil;
        this.fileStorageService = fileStorageService;
        this.fileRepo = fileRepo;
        this.userRepo = userRepo;
        this.docRepo = docRepo;
        this.studentRepo = studRepo;
    }

    @GetMapping("/portfolio")
    public String getPortfolio(Model model, Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());
        List<File> files = fileRepo.findByUserOrderByAssignmentId(currentUser);

        model.addAttribute("files", files);


        //pass username to header fragment
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "portfolio/portfolio";
    }

    @GetMapping("/uploadfile")
    public String getUploadFile(Model model, Principal principal) {
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");

        //get all enrollments
        Student student = studentRepo.findByUserId(currentUser);
        Set<Assignment> list = student.getAssignments();

        model.addAttribute("list", list);
        return "portfolio/upload";
    }


    @PostMapping("/uploadfile")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("type") String type,
                                      @RequestParam("lists") int assignment, Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());
        for (MultipartFile item : files) {
            portfolioUtil.uploadFile(item, currentUser.getUserId(), type, (long) assignment);
        }

        return "redirect:/portfolio";
    }

    @GetMapping("/{studentNumber}/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable("studentNumber") String studentNumber, HttpServletRequest request, HttpServletResponse response) {
        Resource file = this.fileStorageService.loadFileAsResource(fileName, studentNumber);

        String contentType = "application/octet-stream";
        response.setContentType(contentType);

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + fileName).body(file);
    }


    @GetMapping("/deletefile/{id}")
    public String deleteFile(@PathVariable("id") int id) {
        File doc = fileRepo.findByFileId(id).orElseThrow(() -> new IllegalArgumentException("Invalid documentation Id:" + id));
        fileRepo.delete(doc);

        return "redirect:/portfolio";
    }

    @GetMapping("/documentation")
    public String getDocumentation(Model model, Principal principal) {
        List<Documentation> files = docRepo.findAll();
        model.addAttribute("files", files);
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "documentation/documentation";
    }

    @GetMapping("/uploaddocumentation")
    public String getUploadDoc(Model model, Principal principal) {
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name", currentUser.getFirstname() + " " + currentUser.getLastname().substring(0, 1) + ".");
        return "documentation/uploadDocumentation";
    }

    @PostMapping("/uploaddocumentation")
    public String uploadDocumentation(@RequestParam("files") MultipartFile[] files) {

        for (MultipartFile item : files) {
            portfolioUtil.uploadDocumentation(item);
        }

        return "redirect:/documentation";
    }

    @GetMapping("/deletedocumentation/{id}")
    public String deleteDocumentation(@PathVariable("id") int id) {
        Documentation doc = docRepo.findByDocumentId(id).orElseThrow(() -> new IllegalArgumentException("Invalid documentation Id:" + id));
        docRepo.delete(doc);

        return "redirect:/documentation";
    }

}
