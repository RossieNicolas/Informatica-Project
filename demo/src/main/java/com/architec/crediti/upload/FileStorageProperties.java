package com.architec.crediti.upload;

import java.security.Principal;

import com.architec.crediti.models.User;
import com.architec.crediti.repositories.UserRepository;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    UserRepository userRepo;

    public String getUploadDir() {
        return uploadDir + "";
    }

    public void setUploadDir(String uploadDir, Principal principal) {
        User currentUser = userRepo.findByEmail(principal.getName());

        this.uploadDir = uploadDir + "/" + currentUser.getUserId();
    }
}
