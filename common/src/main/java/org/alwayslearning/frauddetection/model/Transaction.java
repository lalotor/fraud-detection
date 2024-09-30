package org.alwayslearning.frauddetection.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Transaction implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull(message = "Amount cannot be null")
  private Double amount;
  @NotNull(message = "Currency cannot be null")
  private String currency;
  @NotNull(message = "Timestamp cannot be null")
  @Column(name = "creationDate")
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

  @Override
  public String toString() {
    return "Transaction{" + "id=" + id + ", amount=" + amount + ", currency='" + currency + '\'' + ", timestamp=" + timestamp + ", sourceAccount='" + sourceAccount + '\'' + ", destinationAccount='" + destinationAccount + '\'' + '}';
  }
}

