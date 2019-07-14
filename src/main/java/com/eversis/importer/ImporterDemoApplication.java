package com.eversis.importer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.eversis"})
@EnableScheduling
public class ImporterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImporterDemoApplication.class, args);
    }
}
