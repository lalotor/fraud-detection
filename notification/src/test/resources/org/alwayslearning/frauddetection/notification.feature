Feature: Fraud Notification Processing
  Scenario: Process a non-fraudulent transaction
    Given a valid notification
    When the notification is processed
    Then the notification should not raise a security alert

  Scenario: Process a fraudulent transaction
    Given a fraudulent notification
    When the notification is processed
    Then the notification should raise a security alert
