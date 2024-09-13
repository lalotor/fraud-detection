package org.alwayslearning.frauddetection.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

  @Bean
  public MessageChannel fraudAnalysisChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel notificationChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel outputChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel notificationOutputChannel() {
    return new DirectChannel();
  }

}

