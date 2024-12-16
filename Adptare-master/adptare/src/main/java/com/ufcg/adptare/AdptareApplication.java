package com.ufcg.adptare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import me.paulschwarz.springdotenv.DotenvPropertySource;

@SpringBootApplication
public class AdptareApplication {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

    	// Add DotenvPropertySource to environment before registering components
    	DotenvPropertySource.addToEnvironment(applicationContext.getEnvironment());
		SpringApplication.run(AdptareApplication.class, args);
	}

}
