package org.alwayslearning.frauddetection.web;

import org.alwayslearning.frauddetection.TransactionApplication;
import org.alwayslearning.frauddetection.model.Transaction;
import org.alwayslearning.frauddetection.repository.TransactionRepository;
import org.alwayslearning.frauddetection.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EntityScan(basePackageClasses = Transaction.class)
@SpringBootTest(classes = TransactionApplication.class)
@EnableJpaRepositories(basePackageClasses = TransactionRepository.class)
class TransactionServiceTest {

  @Autowired
  CacheManager cacheManager;

  @Autowired
  TransactionRepository repository;

  @BeforeEach
  void setUp() {
    repository.save(TestUtils.getTransactionValid());
    Transaction transaction = TestUtils.getTransactionValid();
    transaction.setId(2L);
    repository.save(transaction);
  }

  @Test
  @Disabled
  void givenTransactionThatShouldBeCached_whenFindById_thenResultShouldBePutInCache() {
    Optional<Transaction> tx2 = repository.findById(2L);

    assertEquals(tx2, getCachedBook(2L));
  }

  private Optional<Transaction> getCachedBook(Long id) {
    return ofNullable(cacheManager.getCache("transactions")).map(c -> c.get(id, Transaction.class));
  }
}
