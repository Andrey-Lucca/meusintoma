package br.com.meusintoma.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DotenvApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure().load();

        dotenv.entries().forEach(entry -> {
            System.out.println("[Dotenv Loaded] " + entry.getKey() + " = " + entry.getValue());
            System.setProperty(entry.getKey(), entry.getValue());
        });
    }
}