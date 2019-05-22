package com.architec.crediti.controllers;

import com.architec.crediti.models.Documentation;
import com.architec.crediti.repositories.DocumentationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import com.architec.crediti.repositories.FileRepository;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.upload.FileStorageService;
import com.architec.crediti.models.File;
import com.architec.crediti.models.User;
import com.architec.crediti.utils.PortfolioUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/*
Some functions from
https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example/blob/master/src/main/java/com/example/filedemo/controller/FileController.java
 */

@Controller
public class PortfolioController {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    private final PortfolioUtil portfolioUtil;

    private final FileStorageService fileStorageService;

    private final FileRepository fileRepo;

    private final UserRepository userRepo;

    private final DocumentationRepository docRepo;

    @Autowired
    public PortfolioController(PortfolioUtil portfolioUtil, FileStorageService fileStorageService, FileRepository fileRepo, UserRepository userRepo, DocumentationRepository docRepo) {
        this.portfolioUtil = portfolioUtil;
        this.fileStorageService = fileStorageService;
        this.fileRepo = fileRepo;
        this.userRepo = userRepo;
        this.docRepo = docRepo;
    }

    @GetMapping("/portfolio")
    public String getPortfolio(Model model, Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());
        List<File> files = fileRepo.findByUser(currentUser);
        model.addAttribute("files", files);
        //pass username to header fragment
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "portfolio";
    }

    @GetMapping("/uploadfile")
    public String getUploadFile(Model model, Principal principal) {
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "upload";
    }


    @PostMapping("/uploadfile")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());

        for (MultipartFile item: files) {
            portfolioUtil.uploadFile(item, currentUser.getUserId());
        }

        return "redirect:/portfolio";
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request, Principal principal) {
        // Load file as Resource
        User currentUser = userRepo.findByEmail(principal.getName());
        Resource resource = fileStorageService.loadFileAsResource(fileName, currentUser.getUserId() + "");

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "documentation";
    }

    @GetMapping("/uploaddocumentation")
    public String getUploadDoc(Model model, Principal principal) {
        //pass username to header fragment
        User currentUser = userRepo.findByEmail(principal.getName());
        model.addAttribute("name",currentUser.getFirstname() + " " + currentUser.getLastname().substring(0,1) + ".");
        return "uploadDocumentation";
    }

    @PostMapping("/uploaddocumentation")
    public String uploadDocumentation(@RequestParam("files") MultipartFile[] files) {

        for (MultipartFile item: files) {
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
