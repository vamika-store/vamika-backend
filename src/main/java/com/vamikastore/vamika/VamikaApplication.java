package com.vamikastore.vamika;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.vamikastore.vamika.repositories","com.vamikastore.vamika.auth.repositories"})
public class VamikaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VamikaApplication.class, args);
	}

}
