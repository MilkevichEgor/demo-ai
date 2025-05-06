package com.fusiontech.demo_ai.controller;

//import com.fusiontech.demo_ai.service.OpenService;

import com.fusiontech.demo_ai.dto.GeminiResponse;
import com.fusiontech.demo_ai.service.GeminiAiService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class OpenAiController {

  private final GeminiAiService geminiAiService;

  @PostMapping("/ai/generate")
  public String generate(@RequestBody String prompt) {
	return geminiAiService.sendPromptToGeminiChat(prompt);
  }

  @PostMapping("/ai/generate1")
  public String generate1(@RequestBody String prompt) {
	return geminiAiService.sendPromptToChat(prompt);
  }

  @PostMapping("/ai/generate-json")
  public String generateJsonFormat(@RequestBody String prompt) {
	return geminiAiService.sendPromptToGeminiChatJson(prompt);
  }
}
