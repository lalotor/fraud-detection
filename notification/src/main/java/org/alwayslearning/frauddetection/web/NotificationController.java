package org.alwayslearning.frauddetection.web;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import org.alwayslearning.frauddetection.model.FraudNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fraud-detection")
@Timed("fraud-detection.notification")
public class NotificationController {

  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @PostMapping("/notifications")
  public ResponseEntity<String> handleNotification(@Valid @RequestBody FraudNotification fraudNotification) {
    notificationService.processNotification(fraudNotification);
    return ResponseEntity.ok("Notification processed");
  }
}


