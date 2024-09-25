Feature: Fraud Analysis Service
  Scenario: Analyzing a non-fraudulent transaction
    Given a setup for a transaction response
    Given a transaction with amount below fraud threshold
    When the transaction is analyzed
    Then the transaction should not be flagged as fraudulent

  Scenario: Analyzing a fraudulent transaction
    Given a setup for a transaction response
    Given a transaction with amount above fraud threshold
    When the transaction is analyzed
    Then the transaction should be flagged as fraudulent
