package com.github.zzzummm272;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BootJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootJavaApplication.class, args);
	}
}