Feature: Transaction processing
  Scenario: Valid transaction processing
    Given a setup for a valid transaction response
    Given a valid transaction
    When the transaction is processed
    Then the transaction should be successful

  Scenario: Invalid transaction processing
    Given a setup for an invalid transaction response
    Given an invalid transaction
    When the transaction is processed
    Then the transaction should be rejected
