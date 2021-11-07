package com.loja.LojaPw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class LojaPwApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaPwApplication.class, args);
		new BCryptPasswordEncoder().encode("123");
	}
}
