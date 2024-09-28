package org.alwayslearning.frauddetection.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class  FraudNotification {
  @Setter
  @Getter
  @NotNull(message = "Transaction cannot be null")
  private Transaction transaction;
  private boolean isFraudulent;

  // Constructors, getters, and setters
  public FraudNotification() {
  }

  public FraudNotification(Transaction transaction, boolean isFraudulent) {
    this.transaction = transaction;
    this.isFraudulent = isFraudulent;
  }

  public FraudNotification(Transaction transaction) {
    this.transaction = transaction;
  }

  @JsonProperty("isFraudulent")
  public boolean isFraudulent() {
    return isFraudulent;
  }

  @JsonProperty("isFraudulent")
  public void setFraudulent(boolean fraudulent) {
    isFraudulent = fraudulent;
  }

  @Override
  public String toString() {
    return "FraudNotification{" + "transaction-id=" + transaction.getId() + ", isFraudulent=" + isFraudulent + '\'' + '}';
  }
}

