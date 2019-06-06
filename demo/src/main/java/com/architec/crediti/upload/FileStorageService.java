package com.architec.crediti.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
from https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example/blob/master/src/main/java/com/example/filedemo/service/FileStorageService.java
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    ex);
        }
    }

    public String storeFile(MultipartFile file, String userID) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Mapje maken
            File dir = makeDir(userID);

            // Save file
            if (dir.isDirectory()) {
                File serverFile = new File(dir, fileName);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(file.getBytes());
                stream.close();
            }
            else {
                if(dir.mkdir()){
                    File serverFile = new File(dir, fileName);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(file.getBytes());
                    stream.close();
                }
            }

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private File makeDir(String userId) {
        File dir = new File(fileStorageLocation + File.separator + userId);

        if (!dir.exists()) {
            new File(dir.getPath()).mkdirs();
        }
        return dir;

    }

    public Resource loadFileAsResource(String fileName, String userId) {
        return new FileSystemResource(this.fileStorageLocation + File.separator + userId + File.separator + fileName);
    }
}
