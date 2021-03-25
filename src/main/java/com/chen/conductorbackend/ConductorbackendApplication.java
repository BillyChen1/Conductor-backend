package com.chen.conductorbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.chen.conductorbackend.mapper")
public class ConductorbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConductorbackendApplication.class, args);
    }

}
