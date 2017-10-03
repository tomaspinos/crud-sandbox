package org.jaweze.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class UiVaadinApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiVaadinApplication.class);
    }
}
