package com.skytouch.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {"com.skytouch.task.commons.config.kafka",
		                       "com.skytouch.task.commons.event.services.*",
		                       "com.skytouch.task.pagecontrollers.*",
		                       "com.skytouch.task.controllers",
		                       "com.skytouch.task.pagecontrollers",
		                       "com.skytouch.task.services.*"})
@EnableKafka
public class FrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}

	@Bean
	public RestTemplate initRestTemplate() {
		return new RestTemplate();
	}
}
