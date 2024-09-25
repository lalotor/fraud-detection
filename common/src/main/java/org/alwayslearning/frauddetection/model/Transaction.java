package org.alwayslearning.frauddetection.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "Amount cannot be null")
  private Double amount;
  @NotNull(message = "Currency cannot be null")
  private String currency;
  @NotNull(message = "Timestamp cannot be null")
  private LocalDateTime timestamp;
  @NotNull(message = "Source account cannot be null")
  private String sourceAccount;
  @NotNull(message = "Destination account cannot be null")
  private String destinationAccount;

  // Constructors, getters, and setters
  public Transaction() {
  }

  public Transaction(Double amount, String currency, LocalDateTime timestamp, String sourceAccount, String destinationAccount) {
    this.amount = amount;
    this.currency = currency;
    this.timestamp = timestamp;
    this.sourceAccount = sourceAccount;
    this.destinationAccount = destinationAccount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getSourceAccount() {
    return sourceAccount;
  }

  public void setSourceAccount(String sourceAccount) {
    this.sourceAccount = sourceAccount;
  }

  public String getDestinationAccount() {
    return destinationAccount;
  }

  public void setDestinationAccount(String destinationAccount) {
    this.destinationAccount = destinationAccount;
  }

  @Override
  public String toString() {
    return "Transaction{" + "id=" + id + ", amount=" + amount + ", currency='" + currency + '\'' + ", timestamp=" + timestamp + ", sourceAccount='" + sourceAccount + '\'' + ", destinationAccount='" + destinationAccount + '\'' + '}';
  }
}

