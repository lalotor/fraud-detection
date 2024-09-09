package org.alwayslearning.frauddetection.model;

public class FraudNotification {
  private Transaction transaction;
  private boolean isFraudulent;

  // Constructors, getters, and setters
  public FraudNotification() {
  }

  public FraudNotification(Transaction transaction, boolean isFraudulent) {
    this.transaction = transaction;
    this.isFraudulent = isFraudulent;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    this.transaction = transaction;
  }

  public boolean isFraudulent() {
    return isFraudulent;
  }

  public void setFraudulent(boolean fraudulent) {
    isFraudulent = fraudulent;
  }
}

