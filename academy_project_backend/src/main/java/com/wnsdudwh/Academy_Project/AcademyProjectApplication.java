package com.wnsdudwh.Academy_Project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AcademyProjectApplication
{
	public static void main(String[] args) {
		SpringApplication.run(AcademyProjectApplication.class, args);
	}
}
