package org.alwayslearning.frauddetection.util;

import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;

import java.time.LocalDateTime;

public class TestUtils {

  public static final String ACCOUNT_1 = "DRT45S99IJY2S";
  public static final String ACCOUNT_2 = "DRT45S99IJOK7";

  private TestUtils () {}

  public static Transaction getTransactionValid() {
    Transaction transactionL = new Transaction(1000.0, "COP", LocalDateTime.now(), ACCOUNT_1, ACCOUNT_2);
    transactionL.setId(1L);

    return transactionL;
  }

  public static Transaction getTransactionFraudulent() {
    Transaction transactionL = new Transaction(100000.0, "COP", LocalDateTime.now(), ACCOUNT_1, ACCOUNT_2);
    transactionL.setId(1L);

    return transactionL;
  }

  public static FraudNotification getFraudNotificationValid() {
    return new FraudNotification(getTransactionValid(), false);
  }

  public static FraudNotification getFraudNotificationFraudulent() {
    return new FraudNotification(getTransactionFraudulent(), true);
  }

}
