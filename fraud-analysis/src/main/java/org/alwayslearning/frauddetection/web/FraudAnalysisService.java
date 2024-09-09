package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@Service
public class FraudAnalysisService {

  @Autowired
  private MessageChannel notificationChannel;

  public boolean analyzeTransaction(Transaction transaction) {
    boolean isFraudulent = performAnalysis(transaction);
    notificationChannel.send(MessageBuilder.withPayload(new FraudNotification(transaction, isFraudulent))
        .setReplyChannelName("outputChannel")
        .build());

    return isFraudulent;
  }

  private boolean performAnalysis(Transaction transaction) {
    return transaction.getAmount() > 99999;
  }
}



