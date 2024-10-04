package org.alwayslearning.frauddetection.web;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/fraud-detection")
@Timed("fraud-detection.fraud-analysis")
public class FraudAnalysisController {

  private final FraudAnalysisService fraudAnalysisService;

  public FraudAnalysisController(FraudAnalysisService fraudAnalysisService) {
    this.fraudAnalysisService = fraudAnalysisService;
  }

  @PostMapping("/analyze")
  public ResponseEntity<Boolean> analyzeTransaction(@Valid @RequestBody Transaction transaction) throws ExecutionException, InterruptedException {
    CompletableFuture<Boolean> isFraudulentFuture = fraudAnalysisService.analyzeTransaction(transaction);
    return ResponseEntity.ok(isFraudulentFuture.get());
  }
}

