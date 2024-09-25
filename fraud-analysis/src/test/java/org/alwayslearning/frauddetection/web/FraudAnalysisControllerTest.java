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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FraudAnalysisControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @MockBean
  private FraudAnalysisService fraudAnalysisService;

  private Transaction transaction;

  @BeforeEach
  void setup() {
    transaction = TestUtils.getTransactionValid();
    when(fraudAnalysisService.analyzeTransaction(any(Transaction.class))).thenReturn(false);
  }

  @Test
  void testAnalyzeTransactionPositive() {
    ResponseEntity<Boolean> response = restTemplate.postForEntity(getRootUrl() + "/analyze", transaction,
        Boolean.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isFalse();
    verify(fraudAnalysisService, times(1)).analyzeTransaction(any(Transaction.class));
  }

  @Test
  void testAnalyzeTransactionNegative() {
    transaction = TestUtils.getTransactionFraudulent();
    when(fraudAnalysisService.analyzeTransaction(any(Transaction.class))).thenReturn(true);

    ResponseEntity<Boolean> response = restTemplate.postForEntity(getRootUrl() + "/analyze", transaction,
        Boolean.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isTrue();
    verify(fraudAnalysisService, times(1)).analyzeTransaction(any(Transaction.class));
  }

  @Test
  void testAnalyzeTransactionWithInvalidInput() {
    transaction = null;

    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/analyze", transaction, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    verify(fraudAnalysisService, times(0)).analyzeTransaction(any(Transaction.class));
  }

  @Test
  void testAnalyzeTransactionWithUnsupportedMediaType() {
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

    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/analyze", transactionJSON,
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    verify(fraudAnalysisService, times(0)).analyzeTransaction(any(Transaction.class));
  }

  private String getRootUrl() {
    return "http://localhost:" + port + "/fraud-detection";
  }

}
