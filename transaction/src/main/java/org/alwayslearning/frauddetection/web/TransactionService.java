package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.model.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.support.MessageBuilder;

@Service
public class TransactionService {

  @Autowired
  private TransactionRepository transactionRepository;

//  @Autowired
//  private MessageChannel fraudAnalysisChannel; // Defined in the messaging configuration

  public void processTransaction(Transaction transaction) {
    if (validateTransaction(transaction)) {
      transactionRepository.save(transaction);
//      fraudAnalysisChannel.send(MessageBuilder.withPayload(transaction).build());
    }
  }

  public List<Transaction> getAllTransactions() {
      return transactionRepository.findAll();
  }

  private boolean validateTransaction(Transaction transaction) {
    // Implement validation logic
    return true;
  }
}

