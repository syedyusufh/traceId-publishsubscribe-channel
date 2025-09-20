package com.integration.sample.config;

import static org.springframework.integration.handler.LoggingHandler.Level.ERROR;
import static org.springframework.integration.handler.LoggingHandler.Level.INFO;

import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

import io.micrometer.observation.ObservationRegistry;

@Configuration
public class ErrorFlowConfig {

	@Bean
	MessageChannel appErrorChannel(ObservationRegistry observationRegistry) {

		var errorChannel = new PublishSubscribeChannel(Executors.newSingleThreadExecutor());
		errorChannel.registerObservationRegistry(observationRegistry);
		return errorChannel;
	}

	@Bean
	IntegrationFlow appErrorChannelFlow() {

		// @formatter:off
		return IntegrationFlow.from(appErrorChannel(null))
							.<MessagingException> log(ERROR, m -> "Exception: " + m.getPayload().getCause().getMessage())
							.log(INFO, m -> "This IntegrationFlow's job is ONLY to log the exceptions with the same traceId")
							.get();
		// @formatter:on
	}

}
