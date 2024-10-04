package org.alwayslearning.frauddetection.web;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.alwayslearning.frauddetection.discovery.DiscoveryClientService;
import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
@Slf4j
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final MessageChannel fraudAnalysisChannel;
  private final DiscoveryClientService clientService;
  private final RestTemplate restTemplate;
  @Value("${fraud-analysis.service.id}")
  private String fraudAnalysisServicesId;
  @Value("${fraud-analysis.service.endpoint}")
  private String fraudAnalysisServicesEndpoint;

  public TransactionService(TransactionRepository transactionRepository,
                            @Qualifier("fraudAnalysisChannel") MessageChannel fraudAnalysisChannel,
                            DiscoveryClientService clientService, RestTemplate restTemplate) {
    this.transactionRepository = transactionRepository;
    this.fraudAnalysisChannel = fraudAnalysisChannel;
    this.clientService = clientService;
    this.restTemplate = restTemplate;
  }

  @CircuitBreaker(name = "CircuitBreakerService")
  @Async("taskExecutor")
  public CompletableFuture<Boolean> processTransaction(Transaction transaction) {
    log.debug("Transaction to process: {}", transaction);
    transactionRepository.save(transaction);
    if (validateTransaction(transaction)) {
      fraudAnalysisChannel.send(MessageBuilder.withPayload(transaction).setReplyChannelName("outputChannel").build());

      return CompletableFuture.completedFuture(true);
    } else {
      return CompletableFuture.completedFuture(false);
    }
  }

  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
  }

  public Transaction getTransactionById(Long id) {
    return transactionRepository.findById(id).orElse(null);
  }

  private boolean validateTransaction(Transaction transaction) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Transaction> requestEntity = new HttpEntity<>(transaction, headers);
    String serviceURL = clientService.getServiceUrl(fraudAnalysisServicesId) + fraudAnalysisServicesEndpoint;
    ResponseEntity<Boolean> response = restTemplate.postForEntity(serviceURL, requestEntity, Boolean.class);

    return Boolean.FALSE.equals(response.getBody());
  }

}
