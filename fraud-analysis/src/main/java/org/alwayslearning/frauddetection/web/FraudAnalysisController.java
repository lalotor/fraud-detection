package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fraud-detection")
public class FraudAnalysisController {

  @Autowired
  private FraudAnalysisService fraudAnalysisService;

  @PostMapping("/analyze")
  public ResponseEntity<Boolean> analyzeTransaction(@RequestBody Transaction transaction) {
    return ResponseEntity.ok(fraudAnalysisService.analyzeTransaction(transaction));
  }
}

