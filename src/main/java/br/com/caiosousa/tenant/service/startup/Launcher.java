package br.com.caiosousa.tenant.service.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "br.com.caiosousa.tenant.service", "br.com.caiosousa.tenant.spring" })
@EnableAutoConfiguration
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }
    
}
