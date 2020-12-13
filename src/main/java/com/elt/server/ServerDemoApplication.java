package com.elt.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.elt.server.mapper")
public class ServerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerDemoApplication.class, args);
    }

}
