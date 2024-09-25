package org.alwayslearning.frauddetection.util;

import org.alwayslearning.frauddetection.model.Transaction;

import java.time.LocalDateTime;

public class TestUtils {

  private TestUtils () {}

  public static Transaction getTransactionValid() {
    Transaction transactionL = new Transaction(1000.0, "COP", LocalDateTime.now(), "DRT45S99IJY2S", "DRT45S99IJOK7");
    transactionL.setId(1L);

    return transactionL;
  }

  public static Transaction getTransactionFraudulent() {
    Transaction transactionL = new Transaction(100000.0, "COP", LocalDateTime.now(), "DRT45S99IJY2S", "DRT45S99IJOK7");
    transactionL.setId(1L);

    return transactionL;
  }
}
