package com.fusiontech.demo_ai.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCalendarService {

  private final OAuth2AuthorizedClientService clientService;


  @Tool(description = "Show me events on the calendar")
  public List<Event> getCalendarEvents() {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	String principalName = auth.getName();

	OAuth2AuthorizedClient client =
		clientService.loadAuthorizedClient("google", principalName);


	String token = client.getAccessToken().getTokenValue();


	log.info("{}", token);
	log.info("log-1");
	try {
	  GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(token, null));
	  HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

	  Calendar calendar = new Calendar.Builder(
		  GoogleNetHttpTransport.newTrustedTransport(),
		  GsonFactory.getDefaultInstance(),
		  requestInitializer
	  )
		  .setApplicationName("demo-ai")
		  .build();

	  Events events = calendar.events().list("primary")
		  .setMaxResults(10)
		  .setOrderBy("startTime")
		  .setSingleEvents(true)
		  .execute();

	  log.info("log-2");
	  return events.getItems();
	} catch (Exception e) {
	  throw new RuntimeException(e);
	}
  }

  @Tool(description = "print method")
  public void print() {
	log.info("pring");
  }

  private String authenticationMethod(Authentication authentication) {
	if (authentication.getPrincipal() instanceof Jwt jwt) {
	  // Если это Jwt (OAuth2 Resource Server)
	  log.info("OAuth2 аутентификация с JWT");
	  return jwt.getTokenValue();

	} else if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
	  // Если это OIDC User
	  log.info("OIDC аутентификация с OIDC");
	  return oidcUser.getIdToken().getTokenValue();
	}
	return "";
  }
}
