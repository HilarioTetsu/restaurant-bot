package com.restaurante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class RestauranteBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestauranteBotApplication.class, args);
    }
}