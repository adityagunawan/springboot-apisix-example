package com.bcal.o2o.masterdata.entities.repositories;

import com.bcal.o2o.masterdata.entities.MposTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MposTransactionRepository extends JpaRepository<MposTransactionEntity, Long> {
}
