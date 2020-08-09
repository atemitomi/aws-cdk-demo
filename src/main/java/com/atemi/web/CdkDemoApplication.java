package com.atemi.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.atemi")
public class CdkDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CdkDemoApplication.class, args);
    }
}
