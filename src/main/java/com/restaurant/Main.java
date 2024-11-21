package com.restaurant;

import com.restaurant.gui.MainApplication;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    private static ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        springContext = SpringApplication.run(Main.class, args);
        Application.launch(MainApplication.class, args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}