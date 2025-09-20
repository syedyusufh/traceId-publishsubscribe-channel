package com.integration.sample.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.MessageChannel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Configuration
public class SampleFlowConfig {

	private MessageChannel appErrorChannel;

	@Bean
	IntegrationFlow sampleFlow() {

		// @formatter:off
		return IntegrationFlow.fromSupplier(() -> "Good Day", p -> p.poller(pollerSpec()))
							.enrichHeaders(hdrSpec -> hdrSpec.errorChannel(appErrorChannel, true))
							.handle((payload, headers) -> {
								log.info("Supplied Message is: {}", payload);
								
								if ("Good Day".equals(payload))
									throw new RuntimeException("let us check the traceId");
								
								return payload;
							})
							.handle(m -> log.info("Flow ends"))
							.get();
		// @formatter:on
	}

	@Bean
	PollerSpec pollerSpec() {

		// @formatter:off
		return Pollers.fixedDelay(Duration.ofSeconds(10))
					.errorChannel(appErrorChannel);
		// @formatter:on
	}

}
