package com.java1234;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java1234.mapper")
public class Java1234Vote3Application {

    public static void main(String[] args) {
        SpringApplication.run(Java1234Vote3Application.class, args);
    }

}
