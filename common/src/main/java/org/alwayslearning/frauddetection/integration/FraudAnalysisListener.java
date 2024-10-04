package org.alwayslearning.frauddetection.integration;

import lombok.extern.slf4j.Slf4j;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FraudAnalysisListener {
  public void onMessage(Message<Transaction> message) {
    Transaction transaction = message.getPayload();
    log.debug("Received transaction for analysis: {}", transaction);
  }
}
