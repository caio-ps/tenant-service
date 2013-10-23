package br.com.caiosousa.tenant.service.startup;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "br.com.caiosousa.tenant.service", "br.com.caiosousa.spring" })
@EnableAutoConfiguration
public class Launcher {
}
