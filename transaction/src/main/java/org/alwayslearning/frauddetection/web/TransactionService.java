package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.discovery.DiscoveryClientService;
import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.model.TransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
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


  public boolean processTransaction(Transaction transaction) {
    if (validateTransaction(transaction)) {
      transactionRepository.save(transaction);
      fraudAnalysisChannel.send(MessageBuilder.withPayload(transaction).setReplyChannelName("outputChannel").build());

      return true;
    } else {
      return false;
    }
  }

  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
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

