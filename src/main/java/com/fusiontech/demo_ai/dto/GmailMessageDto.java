package com.fusiontech.demo_ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GmailMessageDto {
  private String id;
  private String subject;
  private String from;
  private String to;
  private String date;
  private String body;
}
