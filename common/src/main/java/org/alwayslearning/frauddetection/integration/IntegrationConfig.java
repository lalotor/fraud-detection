package org.alwayslearning.frauddetection.integration;

import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

  @Bean
  public MessageChannel fraudAnalysisChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow fraudAnalysisFlow(@Qualifier("fraudAnalysisChannel") MessageChannel fraudAnalysisChannel,
                                           FraudAnalysisListener fraudAnalysisListener) {
    return IntegrationFlow.from(fraudAnalysisChannel).handle(message -> fraudAnalysisListener.onMessage((Message<Transaction>) message)).get();
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

