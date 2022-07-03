package com.fournine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableAspectJAutoProxy
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.fournine")
public class FournineAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FournineAppApplication.class, args);
	}

	
}
