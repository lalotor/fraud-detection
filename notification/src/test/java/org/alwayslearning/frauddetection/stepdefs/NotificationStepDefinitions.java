package org.alwayslearning.frauddetection.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.util.TestUtils;
import org.alwayslearning.frauddetection.web.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CucumberContextConfiguration
@SpringBootTest
public class NotificationStepDefinitions {

  @Autowired
  private NotificationService notificationService;

  private FraudNotification fraudNotification;
  private boolean result;

  @Given("a valid notification")
  public void a_valid_notification() {
    fraudNotification = TestUtils.getFraudNotificationValid();
  }

  @Given("a fraudulent notification")
  public void a_fraudulent_notification() {
    fraudNotification = TestUtils.getFraudNotificationFraudulent();
  }

  @When("the notification is processed")
  public void the_notification_is_processed() {
    result = notificationService.processNotification(fraudNotification);
  }

  @Then("the notification should not raise a security alert")
  public void the_notification_should_not_raise_a_security_alert() {
    assertFalse(result);
  }

  @Then("the notification should raise a security alert")
  public void the_notification_should_raise_a_security_alert() {
    assertTrue(result);
  }
}
