package org.alwayslearning.frauddetection.web;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fraud-detection")
@Timed("fraud-detection.transaction")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;

  @PostMapping("/transactions")
  public ResponseEntity<String> createTransaction(@Valid @RequestBody Transaction transaction) {
    boolean isProcessed = transactionService.processTransaction(transaction);
    String responseMessage = isProcessed? "Transaction is being processed" :
        "Transaction is NOT being processed, contact user support";
    return ResponseEntity.ok(responseMessage);
  }

  @GetMapping("/transactions")
  public ResponseEntity<List<Transaction>> getAllTransactions() {
    List<Transaction> transactions = transactionService.getAllTransactions();
    return ResponseEntity.ok(transactions);
  }
}

