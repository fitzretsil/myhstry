package org.myhstry.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

@SpringBootApplication
@EntityScan("org.myhstry.model")
@EnableJpaRepositories("org.myhstry.db")
@EnableConfigurationProperties(StorageProperties.class)
public class MyhstryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyhstryApplication.class, args);
	}
	
	private ISpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
	    SpringTemplateEngine engine = new SpringTemplateEngine();
	    engine.addDialect(new Java8TimeDialect());
	    engine.setTemplateResolver(templateResolver);
	    return engine;
	}
	
	@Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }
}
