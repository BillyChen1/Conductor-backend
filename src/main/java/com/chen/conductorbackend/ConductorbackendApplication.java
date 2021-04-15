package com.chen.conductorbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class ConductorbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConductorbackendApplication.class, args);
    }

}
