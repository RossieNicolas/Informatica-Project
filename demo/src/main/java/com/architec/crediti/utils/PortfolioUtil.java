package com.architec.crediti.utils;

import com.architec.crediti.models.Documentation;
import com.architec.crediti.models.File;
import com.architec.crediti.models.Student;
import com.architec.crediti.models.User;
import com.architec.crediti.repositories.DocumentationRepository;
import com.architec.crediti.repositories.FileRepository;
import com.architec.crediti.repositories.StudentRepository;
import com.architec.crediti.repositories.UserRepository;
import com.architec.crediti.upload.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
public class PortfolioUtil {
    private FileStorageService fileStorageService;

    private FileRepository fileRepo;

    private UserRepository userRepo;

    private DocumentationRepository docRepo;

    private StudentRepository studentRepo;

    @Autowired
    public PortfolioUtil(FileStorageService fileStorageService, FileRepository fileRepo, UserRepository userRepo,
                         DocumentationRepository documRepo, StudentRepository studentRepository) {
        this.fileStorageService = fileStorageService;
        this.fileRepo = fileRepo;
        this.userRepo = userRepo;
        this.docRepo = documRepo;
        this.studentRepo = studentRepository;
    }

    public PortfolioUtil() {

    }

    public void uploadFile(MultipartFile file, long userId, String docType, long assignmentID) {
        User current = userRepo.findByUserId(userId);
        Student st = studentRepo.findByUserId(current);
        String fileName = fileStorageService.storeFile(file, st.getStudentNumber());

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(st.getStudentNumber() + "/downloadFile/")
                .path(fileName).toUriString();

        List<File> docs = fileRepo.findAll();

        for (File item : docs) {
            if (item.getTitle().equals(fileName) && item.getUser().equals(current)) {
                fileRepo.delete(item);
            }
        }

        fileRepo.save(new File(fileName, fileDownloadUri, current, docType, assignmentID));
    }

    public void uploadDocumentation(MultipartFile file) {
        String fileName = fileStorageService.storeFile(file, "documentation");

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/documentation/downloadFile/")
                .path(fileName).toUriString();

        List<Documentation> docs = docRepo.findAll();

        for (Documentation item : docs) {
            if (item.getTitle().equals(fileName)) {
                docRepo.delete(item);
            }
        }

        docRepo.save(new Documentation(fileName, fileDownloadUri));
    }

}
