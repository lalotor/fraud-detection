package org.alwayslearning.frauddetection.web;

import lombok.extern.slf4j.Slf4j;
import org.alwayslearning.frauddetection.discovery.DiscoveryClientService;
import org.alwayslearning.frauddetection.model.FraudNotification;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
@Slf4j
public class FraudAnalysisService {

  private final MessageChannel notificationChannel;

  private final DiscoveryClientService clientService;
  private final RestTemplate restTemplate;
  @Value("${notification.service.id}")
  private String notificationServicesId;
  @Value("${notification.service.endpoint}")
  private String notificationServicesEndpoint;

  public FraudAnalysisService(@Qualifier("notificationChannel") MessageChannel notificationChannel,
                              DiscoveryClientService clientService, RestTemplate restTemplate) {
    this.notificationChannel = notificationChannel;
    this.clientService = clientService;
    this.restTemplate = restTemplate;
  }

  @Cacheable(value = "analyzeTransactions", key="#p0.id", condition="#p0.id != null", unless = "#result == null")
  @Async("taskExecutor")
  public CompletableFuture<Boolean> analyzeTransaction(Transaction transaction) {
    log.debug("Transaction to analyze: {}", transaction);
    boolean isFraudulent = performAnalysis(transaction);
    FraudNotification fraudNotification = new FraudNotification(transaction, isFraudulent);
    notificationChannel.send(MessageBuilder.withPayload(fraudNotification).setReplyChannelName(
        "notificationOutputChannel").build());
    notifyTransaction(fraudNotification);

    return CompletableFuture.completedFuture(isFraudulent);
  }

  private void notifyTransaction(FraudNotification fraudNotification) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<FraudNotification> requestEntity = new HttpEntity<>(fraudNotification, headers);
    String serviceURL = clientService.getServiceUrl(notificationServicesId) + notificationServicesEndpoint;
    ResponseEntity<String> response = restTemplate.postForEntity(serviceURL, requestEntity, String.class);

    log.debug("Notification response: {}", response.getBody());
  }

  private boolean performAnalysis(Transaction transaction) {
    return transaction.getAmount() > 99999;
  }
}



