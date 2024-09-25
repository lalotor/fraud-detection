package org.alwayslearning.frauddetection;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class serves as the entry point for running Cucumber tests.
 * It uses the Cucumber JUnit Platform Engine to integrate Cucumber BDD tests
 * with Spring Boot and JUnit 5.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("org/alwayslearning/frauddetection/transactions.feature")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberIntegrationTest {
  // This class should be empty as it is only used as a configuration
  // entry point for Cucumber tests. All the step definitions will be
  // placed in separate classes under the stepdefs package.
}
