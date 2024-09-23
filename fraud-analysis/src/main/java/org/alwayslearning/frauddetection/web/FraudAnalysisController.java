package org.alwayslearning.frauddetection.web;

import io.micrometer.core.annotation.Timed;
import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fraud-detection")
@Timed("fraud-detection.fraud-analysis")
public class FraudAnalysisController {

  @Autowired
  private FraudAnalysisService fraudAnalysisService;

  @PostMapping("/analyze")
  public ResponseEntity<Boolean> analyzeTransaction(@RequestBody Transaction transaction) {
    return ResponseEntity.ok(fraudAnalysisService.analyzeTransaction(transaction));
  }
}

