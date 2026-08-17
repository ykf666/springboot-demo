package com.springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@ComponentScan("com.springboot.demo.*")
public class BootStrapApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootStrapApplication.class, args);
    }
}
