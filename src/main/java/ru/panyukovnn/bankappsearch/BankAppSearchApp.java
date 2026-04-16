package ru.panyukovnn.bankappsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@SuppressWarnings({"PMD.UseUtilityClass", "checkstyle:HideUtilityClassConstructor"})
public class BankAppSearchApp {

    public static void main(String[] args) {
        SpringApplication.run(BankAppSearchApp.class, args);
    }
}
