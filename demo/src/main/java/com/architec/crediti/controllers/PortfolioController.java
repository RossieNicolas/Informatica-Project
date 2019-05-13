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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import com.architec.crediti.repositories.FileRepository;
import com.architec.crediti.upload.FileStorageService;
import com.architec.crediti.upload.UploadFileResponse;
import com.architec.crediti.models.File;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PortfolioController {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileRepository fileRepo;

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

    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                .path(fileName).toUriString();
        /*
         * File serverFile = new File(dir, fileName); BufferedOutputStream stream = new
         * BufferedOutputStream(new FileOutputStream(serverFile));
         * stream.write(file.getBytes()); stream.close();
         */
        fileRepo.save(new File(fileName, fileDownloadUri));
        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadfile")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
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

}
