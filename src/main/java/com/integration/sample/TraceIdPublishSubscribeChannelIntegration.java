package com.integration.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegrationManagement;

import reactor.core.publisher.Hooks;

@EnableIntegrationManagement(defaultLoggingEnabled = "false", observationPatterns = "*")
@SpringBootApplication
public class TraceIdPublishSubscribeChannelIntegration {

	public static void main(String[] args) {

		Hooks.enableAutomaticContextPropagation();

		SpringApplication.run(TraceIdPublishSubscribeChannelIntegration.class, args);
	}

}
