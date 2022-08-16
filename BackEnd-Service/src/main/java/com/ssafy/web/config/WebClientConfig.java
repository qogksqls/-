package com.ssafy.web.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableCaching
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
				.build();

		WebClient webClient = WebClient.builder().baseUrl("https://i7a606.q.ssafy.io/auth-api")
				.exchangeStrategies(exchangeStrategies) // set exchange strategies
				.build();

		return webClient;
	}
}
