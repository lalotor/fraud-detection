package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class NotificationControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @MockBean
  private NotificationService notificationService;

  private FraudNotification fraudNotification;

  @BeforeEach
  void setup() {
    fraudNotification = getFraudNotificationValid();
    doNothing().when(notificationService).processNotification(any(FraudNotification.class));
  }

  @Test
  void testHandleNotification_Success() {
    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/notifications", fraudNotification, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("Notification processed");
  }

  @Test
  void testHandleNotification_Failure() {
    fraudNotification = null; // Sending a null object to test handling of bad requests
    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/notifications", fraudNotification, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testHandleNotification_IncompleteData() {
    fraudNotification = new FraudNotification();
    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/notifications", fraudNotification, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testHandleNotification_UnsupportedMediaType() {
    String fraudNotificationJSON = """
        {
            "isFraudulent": false,
            "transaction": {
                "id": "0",
                "amount": 1000,
                "currency": "COP",
                "timestamp": "2024-09-08T20:06:58.147Z",
                "sourceAccount": "DRT45S99IJY2S",
                "destinationAccount": "DRT45S99IJOK7"
            }
        }
        """;
    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/notifications", fraudNotificationJSON, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @Test
  void testHandleNotification_ServiceInteraction() {
    ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl() + "/notifications", fraudNotification, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    verify(notificationService, times(1)).processNotification(any(FraudNotification.class));
  }

  private String getRootUrl() {
    return "http://localhost:" + port + "/fraud-detection";
  }

  private FraudNotification getFraudNotificationValid() {
    FraudNotification fraudNotificationL = new FraudNotification();
    Transaction transaction = new Transaction(10000.0, "COP", LocalDateTime.now(), "DRT45S99IJY2S", "DRT45S99IJOK7");
    transaction.setId(1L);
    fraudNotificationL.setTransaction(transaction);

    return fraudNotificationL;
  }

}
