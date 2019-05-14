package com.architec.crediti.controllers;

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
Some functions from
https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example/blob/master/src/main/java/com/example/filedemo/controller/FileController.java
 */

@Controller
public class PortfolioController {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private PortfolioUtil portfolioUtil;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/portfolio")
    public String getPortfolio(Model model) {
        List<File> files = fileRepo.findAll();
        model.addAttribute("files", files);
        return "portfolio";
    }

    @GetMapping("/uploadfile")
    public String getUploadFile() {
        return "upload";
    }

    @PostMapping("/uploadfile")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());

        for (MultipartFile item: files) {
            portfolioUtil.uploadFile(item, currentUser.getUserId());
        }

        //Arrays.stream(files).map(file -> portfolioUtil.uploadFile(file, currentUser.getUserId()));

        // Arrays.asList(files).stream().map(file -> util.uploadFile(file, currentUser.getUserId()))
        //      .collect(Collectors.toList());
        return "redirect:/portfolio";
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

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

    @GetMapping("/documentation")
    public String getDocumentation() {
        return "documentation";
    }

}
