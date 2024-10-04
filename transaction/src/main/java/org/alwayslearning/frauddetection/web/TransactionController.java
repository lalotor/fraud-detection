package org.alwayslearning.frauddetection.web;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/fraud-detection")
@Timed("fraud-detection.transaction")
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/transactions")
  public ResponseEntity<String> createTransaction(@Valid @RequestBody Transaction transaction) throws ExecutionException, InterruptedException {
    CompletableFuture<Boolean> isProcessedFuture = transactionService.processTransaction(transaction);
    String responseMessage = Boolean.TRUE.equals(isProcessedFuture.get())? "Transaction is being processed" :
        "Transaction is NOT being processed, contact user support";
    return ResponseEntity.ok(responseMessage);
  }

  @GetMapping("/transactions")
  public ResponseEntity<List<Transaction>> getAllTransactions() {
    List<Transaction> transactions = transactionService.getAllTransactions();
    return ResponseEntity.ok(transactions);
  }

  @GetMapping("/transactions/{requestedId}")
  public ResponseEntity<Transaction> getTransactionById(@PathVariable("requestedId") Long requestedId) {
    Transaction transaction = transactionService.getTransactionById(requestedId);
    if (transaction != null) {
      return ResponseEntity.ok(transaction);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}

