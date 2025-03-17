package br.com.meusintoma;

import br.com.meusintoma.config.DotenvApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeusintomaApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MeusintomaApplication.class);
        application.addInitializers(new DotenvApplicationContextInitializer());
        application.run(args);
    }
}