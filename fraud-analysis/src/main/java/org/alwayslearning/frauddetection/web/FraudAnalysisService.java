package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.discovery.DiscoveryClientService;
import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.RestTemplate;

@Service
public class FraudAnalysisService {

  @Autowired
  private MessageChannel notificationChannel;

  @Autowired
  private DiscoveryClientService clientService;

  @Value("${notification.service.id}")
  private String notificationServicesId;

  @Value("${notification.service.endpoint}")
  private String notificationServicesEndpoint;

  private final RestTemplate restTemplate;

  public FraudAnalysisService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public boolean analyzeTransaction(Transaction transaction) {
    boolean isFraudulent = performAnalysis(transaction);
    FraudNotification fraudNotification = new FraudNotification(transaction, isFraudulent);
    notificationChannel.send(MessageBuilder.withPayload(fraudNotification)
        .setReplyChannelName("notificationOutputChannel")
        .build());
    notifyTransaction(fraudNotification);

    return isFraudulent;
  }

  private void notifyTransaction(FraudNotification fraudNotification) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<FraudNotification> requestEntity = new HttpEntity<>(fraudNotification, headers);
    String serviceURL = clientService.getServiceUrl(notificationServicesId) + notificationServicesEndpoint;
    ResponseEntity<String> response = restTemplate.postForEntity(serviceURL, requestEntity, String.class);

    System.out.println("Notification response: " + response.getBody());
  }

  private boolean performAnalysis(Transaction transaction) {
    return transaction.getAmount() > 99999;
  }
}



