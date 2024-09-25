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
  private boolean transactionResult;

  @Given("a setup for a transaction response")
  public void a_setup_for_a_transaction_response() {
    ResponseEntity<String> mockResponse = new ResponseEntity<>("Notification processed", HttpStatus.OK);
    when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class)))
        .thenReturn(mockResponse);
  }

  @Given("a valid transaction")
  public void a_valid_transaction() {
    transaction = TestUtils.getTransactionValid();
  }

  @Given("a fraudulent transaction")
  public void a_fraudulent_transaction() {
    transaction = TestUtils.getTransactionFraudulent();
  }

  @When("the transaction is processed")
  public void the_transaction_is_processed() {
    transactionResult = fraudAnalysisService.analyzeTransaction(transaction);
  }

  @Then("the transaction should be successful")
  public void the_transaction_should_be_successful() {
    assertFalse(transactionResult);
  }

  @Then("the transaction should be fraudulent")
  public void the_transaction_should_be_fraudulent() {
    assertTrue(transactionResult);
  }
}
