package br.com.meusintoma;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = br.com.meusintoma.config.DotenvApplicationContextInitializer.class)
class MeusintomaApplicationTests {

	@Test
	void contextLoads() {
	}

}
