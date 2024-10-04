package org.alwayslearning.frauddetection.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.util.TestUtils;
import org.alwayslearning.frauddetection.web.FraudAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest
public class FraudAnalysisStepDefinitions {

  @Autowired
  private FraudAnalysisService fraudAnalysisService;

  @MockBean
  private RestTemplate restTemplate;

  private Transaction transaction;
  private boolean result;

  @Given("a setup for a transaction response")
  public void a_setup_for_a_transaction_response() {
    ResponseEntity<String> mockResponse = new ResponseEntity<>("Notification processed", HttpStatus.OK);
    when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
        .thenReturn(mockResponse);
  }

  @Given("a transaction with amount below fraud threshold")
  public void a_transaction_with_amount_below_fraud_threshold() {
    transaction = TestUtils.getTransactionValid();
  }

  @Given("a transaction with amount above fraud threshold")
  public void a_transaction_with_amount_above_fraud_threshold() {
    transaction = TestUtils.getTransactionFraudulent();
  }

  @When("the transaction is analyzed")
  public void the_transaction_is_analyzed() throws ExecutionException, InterruptedException {
    result = fraudAnalysisService.analyzeTransaction(transaction).get();
  }

  @Then("the transaction should not be flagged as fraudulent")
  public void the_transaction_should_not_be_flagged_as_fraudulent() {
    assertFalse(result);
  }

  @Then("the transaction should be flagged as fraudulent")
  public void the_transaction_should_be_flagged_as_fraudulent() {
    assertTrue(result);
  }
}
