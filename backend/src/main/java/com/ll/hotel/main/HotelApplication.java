package com.ll.hotel.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class, scanBasePackages = "com.ll.hotel")
@EnableRedisRepositories(basePackages = "com.ll.hotel")
@EnableJpaRepositories(basePackages = "com.ll.hotel")
@EntityScan(basePackages = "com.ll.hotel")
public class HotelApplication {
	public static void main(String[] args) {
		SpringApplication.run(HotelApplication.class, args);
	}
}
