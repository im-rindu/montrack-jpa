package com.example.montrack_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.montrack_jpa.jwt.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class MontrackJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MontrackJpaApplication.class, args);
	}

}
