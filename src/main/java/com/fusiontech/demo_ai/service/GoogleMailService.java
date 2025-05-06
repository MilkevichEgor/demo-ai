package com.fusiontech.demo_ai.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoogleMailService {

  public List<Message> getMessage(HttpRequestInitializer requestInitializer) {

	try {
	  Gmail gmailService = new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(),
		  GsonFactory.getDefaultInstance(),
		  requestInitializer)
		  .setApplicationName("demo-ai")
		  .build();

	  ListMessagesResponse listResponse = gmailService.users()
		  .messages()
		  .list("me")
		  .setMaxResults(5L)
		  .execute();

	  List<Message> messages = listResponse.getMessages();

	  List<Message> result = new ArrayList<>();
	  if (messages != null) {
		for (Message msg : messages) {
		  Message detailed = gmailService.users().messages().get("me", msg.getId()).execute();
		  result.add(detailed);
		  //		GmailMessageDto dto = new GmailMessageDto();
//		dto.setId(detailed.getId());
//		dto.setBody(getPlainTextFromMessage(detailed));
//		dto.setSubject(getHeader(detailed, "Subject"));
//		dto.setFrom(getHeader(detailed, "From"));
//		dto.setTo(getHeader(detailed, "To"));
//		dto.setDate(getHeader(detailed, "Date"));
//		result.add(dto);
		}
	  }

	  return result;

	} catch (RuntimeException | GeneralSecurityException | IOException e) {
	  throw new RuntimeException(e);
	}
  }

  private String getPlainTextFromMessage(Message message) {
	MessagePart payload = message.getPayload();
	if (payload == null) return null;

	// Если сразу text/plain
	if ("text/plain".equalsIgnoreCase(payload.getMimeType())) {
	  return decodeMessagePart(payload);
	}

	if (payload.getParts() != null) {
	  for (MessagePart part : payload.getParts()) {
		if ("text/plain".equalsIgnoreCase(part.getMimeType())) {
		  return decodeMessagePart(part);
		}
	  }

	  for (MessagePart part : payload.getParts()) {
		if ("text/html".equalsIgnoreCase(part.getMimeType())) {
		  return decodeMessagePart(part);
		}
	  }
	}

	return null;
  }

  private String decodeMessagePart(MessagePart part) {
	if (part.getBody() != null && part.getBody().getData() != null) {
	  byte[] decodedBytes = Base64.getUrlDecoder().decode(part.getBody().getData());
	  return new String(decodedBytes, StandardCharsets.UTF_8);
	}
	return null;
  }

  private String getHeader(Message message, String headerName) {
	List<MessagePartHeader> headers = message.getPayload().getHeaders();
	if (headers != null) {
	  for (MessagePartHeader header : headers) {
		if (headerName.equalsIgnoreCase(header.getName())) {
		  return header.getValue();
		}
	  }
	}
	return null;
  }
}

