package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.translator.EdiTranslator;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  private static final EdiTranslator EDI_TRANSLATOR = new EdiTranslator();

  public void processNotification(FraudNotification fraudNotification) {
    if (fraudNotification.isFraudulent()) {
      sendAlertToSecurityTeam(fraudNotification.getTransaction());
      notifyCustomer(fraudNotification.getTransaction());
      blockAccount(fraudNotification.getTransaction());
    } else {
      logNonFraudulentActivity(fraudNotification.getTransaction());
    }
  }

  private void sendAlertToSecurityTeam(Transaction transaction) {
    final var transactionInCustomFormat = toCustomFormat(transaction);
    System.out.println("Logic to alert security team in custom format: " + transactionInCustomFormat);
  }

  private void notifyCustomer(Transaction transaction) {
    System.out.println("Logic to notify customer of potential fraud: " + transaction);
  }

  private void blockAccount(Transaction transaction) {
    System.out.println("Logic to block suspicious account: " + transaction);
  }

  private void logNonFraudulentActivity(Transaction transaction) {
    final var transactionInCustomFormat = toCustomFormat(transaction);
    System.out.println("Logic to log non-fraudulent transactions in custom format: " + transactionInCustomFormat);
  }

  private String toCustomFormat(Transaction transaction) {
    if (transaction == null) {
      System.out.println("Invalid transaction");
      return "";
    }
    return EDI_TRANSLATOR.translateToXML(transaction);
  }
}

