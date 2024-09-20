package org.alwayslearning.frauddetection.translator;

import org.alwayslearning.frauddetection.model.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EdiTranslatorTest {

  @Test
  void testTranslateToXML() {
    EdiTranslator translator = new EdiTranslator();
    Transaction transaction = new Transaction(10000.0, "COP", LocalDateTime.now(), "DRT45S99IJY2S", "DRT45S99IJOK7");
    String ediResult = translator.translateToXML(transaction);
    assertNotNull(ediResult);
    assertTrue(ediResult.contains("<sourceAccount>DRT45S99IJY2S</sourceAccount>")); // Check for a segment of the EDI message
  }
}
