package com.bcal.o2o.mpos.sample;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MposTransactionRepository extends JpaRepository<MposTransactionEntity, Long> {
}
