package com.architec.crediti.utils;

import com.architec.crediti.models.File;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.FileRepository;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.upload.FileStorageService;
import com.architec.crediti.upload.UploadFileResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.xml.ws.ServiceMode;

@Service
public class PortfolioUtil {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileRepository fileRepo;

    @Autowired
    private UserRepository userRepo;

    public PortfolioUtil(){

    }

    public UploadFileResponse uploadFile(MultipartFile file, long userId) {
        User current = userRepo.findByUserId(userId);
        String fileName = fileStorageService.storeFile(file, userId +"");

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                .path(fileName).toUriString();

        fileRepo.save(new File(fileName, fileDownloadUri, current));
        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }
}