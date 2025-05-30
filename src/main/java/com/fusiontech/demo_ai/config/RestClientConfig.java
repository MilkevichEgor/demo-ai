package com.fusiontech.demo_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient(){
	return RestClient.builder()
		.baseUrl("https://generativelanguage.googleapis.com")
		.build();
  }
}
