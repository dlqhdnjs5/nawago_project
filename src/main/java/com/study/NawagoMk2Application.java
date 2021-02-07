package com.study;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.study")
public class NawagoMk2Application {

	public static void main(String[] args) {
		SpringApplication.run(NawagoMk2Application.class, args);
	}

}
