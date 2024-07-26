package com.i2kiselev.rinexprocessor.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "app.cli.runner.stub", havingValue = "true")
public class StubCLIRunner {

    @Bean
    public CommandLineRunner run() {
        return  args -> {};
    }
}
