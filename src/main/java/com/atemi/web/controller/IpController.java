package com.atemi.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@Slf4j
@RestController
@RequestMapping("/")
public class IpController {

    @GetMapping("/")
    public String get() {
        return "";
    }

    @GetMapping("/app/ip")
    public String getIp() {
        log.info("Received request for IP");
        try {
            String address = InetAddress.getLocalHost().getHostAddress();
            return String.format("IP: %s", address);
        } catch (Exception e) {
            log.error("Error occurred", e);
            return "Failed to get IP";
        }
    }
}
