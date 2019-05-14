package com.architec.crediti.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
From https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example/blob/master/src/main/java/com/example/filedemo/property/FileStorageProperties.java
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;

    public String getUploadDir() {

        return uploadDir + "";
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
