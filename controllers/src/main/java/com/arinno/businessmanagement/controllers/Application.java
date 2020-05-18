package com.arinno.businessmanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Created by aarinopu on 10/12/2019.
 */

@SpringBootApplication
@ComponentScan("com.arinno.businessmanagement.*")
@EnableJpaRepositories(basePackages = {"com.arinno.businessmanagement.persistence"})
@EntityScan("com.arinno.businessmanagement.model")
//@Import({com.arinno.businessmanagement.auth.SpringSecurityConfig.class})
public class Application implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        String password = "12345";

        for (int i = 0; i < 4; i++) {
            String passwordBcrypt = passwordEncoder.encode(password);
            System.out.println(passwordBcrypt);
        }

        System.out.println("correcto");
    }
}
