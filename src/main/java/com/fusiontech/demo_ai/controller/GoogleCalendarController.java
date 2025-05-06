package com.fusiontech.demo_ai.controller;

import com.fusiontech.demo_ai.service.GoogleCalendarService;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.calendar.model.Event;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GoogleCalendarController {

  private final GoogleCalendarService calendarService;

  @GetMapping("/google/calendar")
  public List<Event> getCalendarEvents(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {

//	String accessToken = authorizedClient.getAccessToken().getTokenValue();


	return calendarService.getCalendarEvents();
  }
}


