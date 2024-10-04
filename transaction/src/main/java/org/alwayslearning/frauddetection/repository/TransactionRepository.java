package org.alwayslearning.frauddetection.repository;

import org.alwayslearning.frauddetection.model.Transaction;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  @Cacheable(value = "transactions", key="#p0", condition="#p0 != null", unless = "#result == null")
  Optional<Transaction> findById(Long id);

}
