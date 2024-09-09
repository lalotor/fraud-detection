package org.alwayslearning.frauddetection.integration;

import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor {

  @ServiceActivator(inputChannel = "fraudAnalysisChannel", outputChannel = "outputChannel")
  public String handleFraudAnalysisMessage(Message<Transaction> message) {
    System.out.println("MessageProcessor.handleFraudAnalysisMessage: " + message.getPayload());
    return "Processed: " + message.getPayload();
  }

  @ServiceActivator(inputChannel = "notificationChannel", outputChannel = "outputChannel")
  public String handleNotificationMessage(Message<FraudNotification> message) {
    System.out.println("MessageProcessor.handleNotificationMessage: " + message.getPayload());
    return "Processed: " + message.getPayload();
  }

  @ServiceActivator(inputChannel = "outputChannel")
  public void handleOutputMessage(String message) {
    System.out.println("MessageProcessor.handleOutputMessage: " + message);
  }
}

