package com.abatplus.routeservicebus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RouteServiceBusApplication {

	@Bean
	public WebClient webClient(WebClient.Builder webClientBuilder) {
		return webClientBuilder.build();
	}

	public static void main(String[] args) {

		SpringApplication.run(RouteServiceBusApplication.class, args);

		}


}
