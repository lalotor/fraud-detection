package org.alwayslearning.frauddetection.exception;

public class TranslatorException extends RuntimeException {
  public TranslatorException() {
  }

  public TranslatorException(String message) {
    super(message);
  }

  public TranslatorException(Throwable cause) {
    super(cause);
  }
}
