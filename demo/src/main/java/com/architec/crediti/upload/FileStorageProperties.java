package com.architec.crediti.upload;

import com.architec.crediti.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.Principal;

/*
From https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example/blob/master/src/main/java/com/example/filedemo/property/FileStorageProperties.java
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    @Autowired
    private UserRepository userRepo;

    public String getUploadDir() {

        return uploadDir + "";
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
