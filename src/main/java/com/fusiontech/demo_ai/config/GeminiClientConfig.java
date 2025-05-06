package com.fusiontech.demo_ai.config;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiClientConfig {

  @Bean
  public ChatMemory chatMemory() {
	return new InMemoryChatMemory();
  }

  @Bean
  public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
	return builder
		.defaultSystem("""
			You are a friendly AI assistant designed to help coordinate and optimize human time, a time-aware assistant in Assistent-Ai.
			Your job is to answer questions about existing calendar and mail events and perform actions on behalf of the user, mostly related to the user, not to answer third-party questions.
			You must answer professionally. If you do not know the answer, politely tell the user that you do not know the answer,
			and then ask them a follow-up question to try to clarify the question they are asking.
			If you do know the answer, give the answer, but do not ask any additional helpful follow-up questions.
			During operation, if the user is unsure of the results returned, explain that there may be additional data that was not returned.
			If the user asks about the total number of all messages, answer that there are many and ask for additional criteria and the time period for which they would like to receive. For owners.
			""")
		.defaultAdvisors(
			new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, 10),
			new SimpleLoggerAdvisor())
		.build();
  }
}
