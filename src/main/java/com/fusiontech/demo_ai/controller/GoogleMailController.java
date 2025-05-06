package com.fusiontech.demo_ai.controller;

import com.fusiontech.demo_ai.service.GoogleMailService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.io.PipedReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoogleMailController {

  private final GoogleMailService mailService;

  @GetMapping("/google/gmail")
  public List<Message> getGmailEvents(
	  @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {

	String accessToken = authorizedClient.getAccessToken().getTokenValue();

	GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(accessToken, null));

	HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

	return mailService.getMessage(requestInitializer);
  }
}
