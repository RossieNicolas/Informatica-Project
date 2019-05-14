package com.architec.crediti.upload;

/*
from https://github.com/callicoder/spring-boot-file-upload-download-rest-api-example/tree/master/src/main/java/com/example/filedemo/exception
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
