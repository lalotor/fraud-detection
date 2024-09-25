package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TransactionControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @MockBean
  private TransactionService transactionService;

  private Transaction transaction;

  @BeforeEach
  void setup() {
    transaction = TestUtils.getTransactionValid();
    when(transactionService.processTransaction(any(Transaction.class))).thenReturn(true);
  }

  @Test
  void testCreateTransactionPositive() {
    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/transactions", transaction,
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("Transaction is being processed");
    verify(transactionService, times(1)).processTransaction(any(Transaction.class));
  }

  @Test
  void testCreateTransactionNegative() {
    transaction = TestUtils.getTransactionFraudulent();
    when(transactionService.processTransaction(any(Transaction.class))).thenReturn(false);

    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/transactions", transaction,
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("Transaction is NOT being processed, contact user support");
    verify(transactionService, times(1)).processTransaction(any(Transaction.class));
  }

  @Test
  void testCreateTransactionWithInvalidInput() {
    transaction = null;

    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/transactions", transaction,
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    verify(transactionService, times(0)).processTransaction(any(Transaction.class));
  }

  @Test
  void testCreateTransactionWithUnsupportedMediaType() {
    String transactionJSON = """
        {
          "id": "0",
          "amount": 1000,
          "currency": "COP",
          "timestamp": "2024-09-08T20:06:58.147Z",
          "sourceAccount": "DRT45S99IJY2S",
          "destinationAccount": "DRT45S99IJOK7"
        }
        """;

    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/transactions", transactionJSON,
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    verify(transactionService, times(0)).processTransaction(any(Transaction.class));
  }

  @Test
  void testConcurrentTransactionHandling() throws InterruptedException {
    // Setup a scenario with concurrent requests
    ExecutorService executor = Executors.newFixedThreadPool(10);
    IntStream.range(0, 10).forEach(i -> executor.submit(() -> {
      Transaction concurrentTransaction = TestUtils.getTransactionValid();
      ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/transactions",
          concurrentTransaction, String.class);
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }));
    executor.shutdown();
    executor.awaitTermination(1, TimeUnit.MINUTES);
  }

  @Test
  void testGetTransactionsWhenEmpty() {
    when(transactionService.getAllTransactions()).thenReturn(Collections.emptyList());

    ResponseEntity<List<Transaction>> response = restTemplate.exchange(getRootUrl() + "/transactions", HttpMethod.GET
        , null, new ParameterizedTypeReference<List<Transaction>>() {
    });

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEmpty();
    verify(transactionService, times(1)).getAllTransactions();
  }


  @Test
  void testGetTransactionsWithMultipleEntries() {
    List<Transaction> transactions = List.of(new Transaction(100.0, "USD", LocalDateTime.now(), "ACC123", "ACC456"),
        new Transaction(200.0, "USD", LocalDateTime.now(), "ACC789", "ACC101"));
    when(transactionService.getAllTransactions()).thenReturn(transactions);

    ResponseEntity<List<Transaction>> response = restTemplate.exchange(getRootUrl() + "/transactions", HttpMethod.GET
        , null, new ParameterizedTypeReference<List<Transaction>>() {
    });

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(2);
    verify(transactionService, times(1)).getAllTransactions();
  }

  @Test
  void testGetTransactionsServiceException() {
    when(transactionService.getAllTransactions()).thenThrow(new RuntimeException("Database error"));

    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/transactions", HttpMethod.GET, null,
        new ParameterizedTypeReference<String>() {
    });

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    verify(transactionService, times(1)).getAllTransactions();
  }

  private String getRootUrl() {
    return "http://localhost:" + port + "/fraud-detection";
  }

}
