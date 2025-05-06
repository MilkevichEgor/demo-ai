package com.fusiontech.demo_ai.service;

import com.fusiontech.demo_ai.dto.GeminiResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiAiService {

  private final ChatClient chatClient;
  private final GoogleCalendarService googleCalendarService;

  public String sendPromptToGeminiChat(String request) {
	// TODO интегрировать взаимодействие AI с Google Mail и Google Calendar

	return chatClient.prompt(request)
		.tools(googleCalendarService)
		.call()
		.content();
  }

  public String sendPromptToChat(String request) {
	return chatClient.prompt(request)
		.call()
		.content();
  }

  public String sendPromptToGeminiChatJson(String request) {
	MapOutputConverter mapOutputConverter = new MapOutputConverter();

	String format = mapOutputConverter.getFormat();

	log.info("{}", format);

	PromptTemplate prompt = new PromptTemplate(request, Map.of("format", format));
	Prompt prompt1 =  new Prompt(prompt.createMessage());

	return chatClient.prompt(prompt1)
		.call()
		.content();
  }

}