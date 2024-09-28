package org.alwayslearning.frauddetection.web;

import lombok.extern.slf4j.Slf4j;
import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.translator.EdiTranslator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

  private static final EdiTranslator EDI_TRANSLATOR = new EdiTranslator();

  public boolean processNotification(FraudNotification fraudNotification) {
    if (fraudNotification.isFraudulent()) {
      sendAlertToSecurityTeam(fraudNotification.getTransaction());
      notifyCustomer(fraudNotification.getTransaction());
      blockAccount(fraudNotification.getTransaction());

      return true;
    } else {
      logNonFraudulentActivity(fraudNotification.getTransaction());

      return false;
    }
  }

  private void sendAlertToSecurityTeam(Transaction transaction) {
    final var transactionInCustomFormat = toCustomFormat(transaction);
    log.debug("Logic to alert security team in custom format: {}", transactionInCustomFormat);
  }

  private void notifyCustomer(Transaction transaction) {
    log.debug("Logic to notify customer of potential fraud: {}", transaction);
  }

  private void blockAccount(Transaction transaction) {
    log.debug("Logic to block suspicious account: {}", transaction);
  }

  private void logNonFraudulentActivity(Transaction transaction) {
    final var transactionInCustomFormat = toCustomFormat(transaction);
    log.debug("Logic to log non-fraudulent transactions in custom format: {}", transactionInCustomFormat);
  }

  private String toCustomFormat(Transaction transaction) {
    if (transaction == null) {
      log.debug("Invalid transaction");
      return "";
    }
    return EDI_TRANSLATOR.translateToXML(transaction);
  }
}

