package com.jl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ApiSportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSportsApplication.class, args);
    }

}
