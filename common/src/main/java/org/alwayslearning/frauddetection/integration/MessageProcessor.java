package org.alwayslearning.frauddetection.integration;

import lombok.extern.slf4j.Slf4j;
import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageProcessor {

  @ServiceActivator(inputChannel = "fraudAnalysisChannel", outputChannel = "outputChannel")
  public String handleFraudAnalysisMessage(Message<Transaction> message) {
    log.debug("MessageProcessor.handleFraudAnalysisMessage: {}", message.getPayload());
    return "Processed: " + message.getPayload();
  }

  @ServiceActivator(inputChannel = "notificationChannel", outputChannel = "outputChannel")
  public String handleNotificationMessage(Message<FraudNotification> message) {
    log.debug("MessageProcessor.handleNotificationMessage: {}", message.getPayload());
    return "Processed: " + message.getPayload();
  }

  @ServiceActivator(inputChannel = "outputChannel")
  public void handleOutputMessage(String message) {
    log.debug("MessageProcessor.handleOutputMessage: {}", message);
  }

  @ServiceActivator(inputChannel = "notificationOutputChannel")
  public void handleNotificationOutputMessage(String message) {
    log.debug("MessageProcessor.handleNotificationOutputMessage: {}", message);
  }
}

