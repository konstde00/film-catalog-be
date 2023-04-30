package com.konstde00.filmcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

@SpringBootApplication
public class FilmcatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmcatalogApplication.class, args);
    }

}
