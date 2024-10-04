package org.alwayslearning.frauddetection.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.util.TestUtils;
import org.alwayslearning.frauddetection.web.TransactionService;
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
public class TransactionStepDefinitions {

  @Autowired
  private TransactionService transactionService;

  @MockBean
  private RestTemplate restTemplate;

  private Transaction transaction;
  private boolean transactionResult;

  @Given("a setup for a valid transaction response")
  public void a_setup_for_a_valid_transaction_response() {
    ResponseEntity<Boolean> mockResponse = new ResponseEntity<>(false, HttpStatus.OK);
    when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(Boolean.class)))
        .thenReturn(mockResponse);
  }

  @Given("a setup for an invalid transaction response")
  public void a_setup_for_an_invalid_transaction_response() {
    ResponseEntity<Boolean> mockResponse = new ResponseEntity<>(true, HttpStatus.OK);
    when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(Boolean.class)))
        .thenReturn(mockResponse);
  }

  @Given("a valid transaction")
  public void a_valid_transaction() {
    transaction = TestUtils.getTransactionValid();
  }

  @Given("an invalid transaction")
  public void an_invalid_transaction() {
    transaction = TestUtils.getTransactionFraudulent();
  }

  @When("the transaction is processed")
  public void the_transaction_is_processed() throws ExecutionException, InterruptedException {
    transactionResult = transactionService.processTransaction(transaction).get();
  }

  @Then("the transaction should be successful")
  public void the_transaction_should_be_successful() {
    assertTrue(transactionResult);
  }

  @Then("the transaction should be rejected")
  public void the_transaction_should_be_rejected() {
    assertFalse(transactionResult);
  }
}
