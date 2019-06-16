package com.architec.crediti;

import com.architec.crediti.upload.FileStorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({FileStorageProperties.class})
public class CreditiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditiApplication.class, args);
    }
}