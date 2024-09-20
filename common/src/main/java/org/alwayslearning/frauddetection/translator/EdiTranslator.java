package org.alwayslearning.frauddetection.translator;

import org.alwayslearning.frauddetection.model.Transaction;
import org.smooks.Smooks;
import org.smooks.api.ExecutionContext;
import org.smooks.io.payload.JavaSource;
import org.smooks.io.payload.StringResult;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public class EdiTranslator {

  private static final String SMOOKS_CONFIG = "smooks-config.xml";

  public String translateToXML(Transaction transaction) {
    Smooks smooks = new Smooks();
    try {
      smooks = new Smooks(SMOOKS_CONFIG);
      ExecutionContext executionContext = smooks.createExecutionContext();
      StringWriter writer = new StringWriter();
      smooks.filterSource(executionContext, new JavaSource(transaction), new StreamResult(writer));

      return writer.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    } finally {
      smooks.close();
    }
  }
}

