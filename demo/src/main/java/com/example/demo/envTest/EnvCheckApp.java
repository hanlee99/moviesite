package com.example.demo.envTest;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


//@SpringBootApplication(scanBasePackages = "com.example.demo.envTest")
public class EnvCheckApp {
    public static void main(String[] args) {
        SpringApplication.run(EnvCheckApp.class, args);
    }
}


//@Component
class EnvCheck {

    @Value("${api.kmdb.key}")
    private String kmdbKey;

    @Value("${api.kobis.key}")
    private String kobisKey;

    @PostConstruct
    public void init() {
        System.out.println("✅ KMDB_KEY = " + kmdbKey);
        System.out.println("✅ KOBIS_KEY = " + kobisKey);
    }
}
