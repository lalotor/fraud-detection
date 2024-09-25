Feature: Transaction processing
  Scenario: Valid transaction processing
    Given a setup for a transaction response
    Given a valid transaction
    When the transaction is processed
    Then the transaction should be successful

  Scenario: Invalid transaction processing
    Given a setup for a transaction response
    Given a fraudulent transaction
    When the transaction is processed
    Then the transaction should be fraudulent
