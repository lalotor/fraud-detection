package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fraud-detection")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping("/transactions")
  public ResponseEntity<String> createTransaction(@RequestBody Transaction transaction) {
    boolean isProcessed = transactionService.processTransaction(transaction);
    String responseMessage = isProcessed? "Transaction is being processed" :
        "Transaction is NOT being processed, contact user support";
    return ResponseEntity.ok(responseMessage);
  }

  @GetMapping("/transactions")
  public ResponseEntity<List<Transaction>> createTransaction() {
    List<Transaction> transactions = transactionService.getAllTransactions();
    return ResponseEntity.ok(transactions);
  }
}

