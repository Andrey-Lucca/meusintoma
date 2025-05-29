package br.com.meusintoma;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class MeusintomaApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();

        // Carrega variáveis do .env como propriedades padrão
        Map<String, Object> envMap = new HashMap<>();
        dotenv.entries().forEach(entry -> {
            System.out.println("[Dotenv Loaded] " + entry.getKey() + " = " + entry.getValue());
            envMap.put(entry.getKey(), entry.getValue());
            System.setProperty(entry.getKey(), entry.getValue()); // opcional
        });

        SpringApplication app = new SpringApplication(MeusintomaApplication.class);
        app.setDefaultProperties(envMap); // <- injeta no Spring Boot diretamente
        app.run(args);
    }
}
