package com.mycompany.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.mycompany")
@EnableJpaRepositories(basePackages = "com.mycompany.persistencia")
@EntityScan(basePackages = "com.mycompany.modelo")
@EnableScheduling // Habilita la ejecución automática en segundo plano
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}